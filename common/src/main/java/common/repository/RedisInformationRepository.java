package common.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        String key = KeyGenerator.generateField(dao);
        logger.info("[*] Saving info key: {}", key);

        try {
            String json = objectMapper.writeValueAsString(dao);
            return redisTemplate.opsForValue().set(key, json)
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
