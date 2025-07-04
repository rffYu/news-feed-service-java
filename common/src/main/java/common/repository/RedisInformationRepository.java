package common.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.constants.RedisConstants;
import common.dao.InformationDao;
import common.dao.TimeSensitiveInformationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class KeyGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String infoField(String source, String dataId, String additionId) {
        return "info:" + source + ":" + dataId + ":" + additionId;
    }

    public static String generateField(InformationDao info) {
        String additionIdString = "";
        if (info instanceof TimeSensitiveInformationDao) {
            TimeSensitiveInformationDao timeInfo = (TimeSensitiveInformationDao) info;
            LocalDateTime dt = timeInfo.getDt() != null ? timeInfo.getDt() : LocalDateTime.now();
            additionIdString = dt.format(FORMATTER);
        }

        return infoField(info.getSource(), info.getInfoId(), additionIdString);
    }
}

class InfoToMapConverter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObjectMapper objectMapper;

    public InfoToMapConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, String> convertToMap(InformationDao info) {
        Map<String, String> result = new HashMap<>();

        // Convert object to tree for flexible manipulation
        ObjectNode node = objectMapper.valueToTree(info);

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode valueNode = field.getValue();

            String valueStr;

            if (valueNode.isNull()) {
                valueStr = "";
            } else if (valueNode.isTextual()) {
                valueStr = valueNode.asText();
            } else if (valueNode.isArray()) {
                // For arrays, convert to JSON string or join by comma if you prefer
                List<String> list = new ArrayList<>();
                for (com.fasterxml.jackson.databind.JsonNode item : valueNode) {
                    list.add(item.asText());
                }
                valueStr = String.join(",", list);
            } else if (valueNode.isObject()) {
                // For embedded objects, convert to JSON string
                valueStr = valueNode.toString();
            } else {
                // For primitives (number, boolean), convert to string
                valueStr = valueNode.asText();
            }

            // Special handling for your LocalDateTime if needed
            if ("dt".equals(key) && info instanceof TimeSensitiveInformationDao) {
                TimeSensitiveInformationDao tsi = (TimeSensitiveInformationDao) info;
                if (tsi.getDt() != null) {
                    valueStr = tsi.getDt().format(FORMATTER);
                }
            }

            result.put(key, valueStr);
        }

        return result;
    }
}

@Repository("redisInformationRepository")
public class RedisInformationRepository implements InformationRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(RedisInformationRepository.class);

    public RedisInformationRepository(ReactiveRedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> save(InformationDao dao) {
        InfoToMapConverter converter = new InfoToMapConverter(objectMapper);
        String key = KeyGenerator.generateField(dao);
        logger.info("[*] Saving info key: {}", key);

        try {
            Map<String, String> dataMap = converter.convertToMap(dao);
            return redisTemplate.opsForHash().putAll(key, dataMap)
                .flatMap(success -> {
                    if (success) {
                        return redisTemplate.expire(key, Duration.ofSeconds(RedisConstants.INFO_EXPIRE_IN_SEC));
                    } else {
                        return Mono.error(new RuntimeException("Failed to set value in Redis"));
                    }
                })
                .then(); // Mono<Void>
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to save DAO to Redis", e));
        }
    }
}
