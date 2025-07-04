package com.newsfeed.listener.consumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.listener.constants.MQConstants;
import com.newsfeed.listener.service.StoreInfoService;

import common.dto.InformationDto;

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

    @RabbitListener(queues = MQConstants.STORE_INFO_QUEUE)
    public void onMessage(@Payload InformationDto payload) {
        storeInfoService.storeInfoAsync(payload);
    }
}
