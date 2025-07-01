package com.newsfeed.listener.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.listener.utils.HtmlCleaner;
import com.newsfeed.listener.constants.RedisConstants;

import common.models.Information;

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

            // Store in Redis
            redisTemplate.opsForValue().set(
                    "info:" + info.getInfoId(),
                    objectMapper.writeValueAsString(info),
                    RedisConstants.INFO_EXPIRE_IN_SEC, TimeUnit.SECONDS
            );

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
