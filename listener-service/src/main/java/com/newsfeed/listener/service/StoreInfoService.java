package com.newsfeed.listener.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.newsfeed.listener.constants.RedisConstants;
import com.newsfeed.listener.utils.HtmlCleaner;

import common.models.Information;
import common.models.TimeSensitiveInformation;
import com.fasterxml.jackson.databind.node.ObjectNode;

class KeyGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String infoField(String source, String dataId, String additioId) {
        return "info:" + source + ":" + dataId + ":" + additioId;
    }

    public static String generateField(Information info) {
        String additionIdString = "";
        if (info instanceof TimeSensitiveInformation) {
            TimeSensitiveInformation timeInfo = (TimeSensitiveInformation) info;
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

    public Map<String, String> convertToMap(Information info) {
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
            if ("dt".equals(key) && info instanceof TimeSensitiveInformation) {
                TimeSensitiveInformation tsi = (TimeSensitiveInformation) info;
                if (tsi.getDt() != null) {
                    valueStr = tsi.getDt().format(FORMATTER);
                }
            }

            result.put(key, valueStr);
        }

        return result;
    }
}

@Service
public class StoreInfoService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(StoreInfoService.class);

    public StoreInfoService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Async
    public void processInfoAsync(Information info) {
        try {
            logger.info("[*] Processing info id: {}", info.getInfoId());

            String infoField = KeyGenerator.generateField(info);

            // Store in Redis
            InfoToMapConverter converter = new InfoToMapConverter(objectMapper);

            Map<String, String> infoMap = converter.convertToMap(info);
            redisTemplate.opsForHash().putAll(infoField, infoMap);
            redisTemplate.expire(infoField, RedisConstants.INFO_EXPIRE_IN_SEC, TimeUnit.SECONDS);

            // Send new info message (simulate)
            sendNewInfoMsg(info);

            // Send dig topic action (simulate)
            String cleanedContent = HtmlCleaner.cleanHtml(info.getContent());
            sendDigTopicAction(info.getInfoId(), info.getTitle() + " " + cleanedContent);

        } catch (Exception e) {
            logger.error("Error processing info", e);
        }
    }

    private void sendNewInfoMsg(Information info) {
        System.out.println("[>] New Info Msg Sent: " + info.getInfoId());
    }

    private void sendDigTopicAction(String infoId, String content) {
        System.out.println("[>] Dig Topic Action: " + infoId + " - " + content);
    }
}
