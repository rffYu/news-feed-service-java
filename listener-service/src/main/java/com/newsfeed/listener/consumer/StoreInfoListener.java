package com.newsfeed.listener.consumer;

import common.models.Information;
import com.newsfeed.listener.service.StoreInfoService;
import com.newsfeed.listener.constants.MQConstants;
import com.newsfeed.listener.consumer.SerializationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StoreInfoListener {

    private final StoreInfoService storeInfoService;
    private final ObjectMapper objectMapper;

    @Autowired
    public StoreInfoListener(StoreInfoService storeInfoService, ObjectMapper objectMapper) {
        this.storeInfoService = storeInfoService;
        this.objectMapper = objectMapper;
    }

    /**
     * This method is equivalent to the Python `queue.consume(on_message)`
     * It listens to the queue named `store_info_queue`.
     */
    @RabbitListener(queues = MQConstants.STORE_INFO_QUEUE)
    public void onMessage(String message) {
        try {
            // Deserialize JSON to Info object
            Information info = SerializationHelper.decodeDataObject(message);

            // Asynchronously process the info
            storeInfoService.processInfoAsync(info);

        } catch (Exception e) {
            e.printStackTrace();
            // Optional: You can log errors or send to dead-letter queue here
        }
    }
}

