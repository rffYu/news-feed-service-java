package com.newsfeed.listener.service;

import common.models.Information;
import com.newsfeed.listener.utils.HtmlCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class StoreInfoService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public StoreInfoService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Async
    public void processInfoAsync(Information info) {
        try {
            System.out.println("[*] Processing info id: " + info.getInfoId());

            // Example: default expiry is 5 days = 432000 seconds
            int expires = 432000;

            // Store in Redis
            redisTemplate.opsForValue().set(
                    "info:" + info.getInfoId(),
                    objectMapper.writeValueAsString(info),
                    expires, TimeUnit.SECONDS
            );

            // Send new info message (simulate)
            sendNewInfoMsg(info);

            // Send dig topic action (simulate)
            String cleanedContent = HtmlCleaner.cleanHtml(info.getContent());
            sendDigTopicAction(info.getInfoId(), info.getTitle() + " " + cleanedContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNewInfoMsg(Information info) {
        System.out.println("[>] New Info Msg Sent: " + info.getInfoId());
    }

    private void sendDigTopicAction(String infoId, String content) {
        System.out.println("[>] Dig Topic Action: " + infoId + " - " + content);
    }
}
