package com.newsfeed.listener.consumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.listener.constants.MQConstants;
import com.newsfeed.listener.service.StoreInfoService;

import common.models.Information;

@Component
public class StoreInfoListener {

    private final StoreInfoService storeInfoService;

    @Autowired
    public StoreInfoListener(StoreInfoService storeInfoService, ObjectMapper objectMapper) {
        this.storeInfoService = storeInfoService;
    }

    @Bean
    public DirectExchange storeInfoExchange() {
        return new DirectExchange(MQConstants.EXCHANGE);
    }

    @Bean
    public Queue storeInfoQueue() {
        return new Queue(MQConstants.STORE_INFO_QUEUE, false);
    }

    @Bean
    public Binding binding(Queue storeInfoQueue, DirectExchange storeInfoExchange) {
        return BindingBuilder
                .bind(storeInfoQueue)
                .to(storeInfoExchange)
                .with(MQConstants.STORE_INFO_ROUTING_KEY);
    }

    @Autowired
    private SerializationHelper serializationHelper;

    /**
     * This method is equivalent to the Python `queue.consume(on_message)`
     * It listens to the queue named `store_info_queue`.
     */
    @RabbitListener(queues = MQConstants.STORE_INFO_QUEUE)
    public void onMessage(String message) {
        try {
            // Deserialize JSON to Info object
            Information info = serializationHelper.decodeDataObject(message);

            // Asynchronously process the info
            storeInfoService.processInfoAsync(info);

        } catch (Exception e) {
            e.printStackTrace();
            // Optional: You can log errors or send to dead-letter queue here
        }
    }
}
