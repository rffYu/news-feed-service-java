package com.newsfeed.listener.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.listener.constants.MQConstants;
import com.newsfeed.listener.service.StoreInfoService;
import com.rabbitmq.client.Channel;
import common.dto.InformationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class StoreInfoListener {

    private final StoreInfoService storeInfoService;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(StoreInfoListener.class);

    @Autowired
    public StoreInfoListener(StoreInfoService storeInfoService, ObjectMapper objectMapper) {
        this.storeInfoService = storeInfoService;
        this.objectMapper = objectMapper;
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

    @RabbitListener(queues = MQConstants.STORE_INFO_QUEUE, ackMode = "MANUAL")
    public void onMessage(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            InformationDto info = objectMapper.readValue(json, InformationDto.class);

            storeInfoService.storeInfoAsync(info)
                .doOnSuccess(v -> {
                    try {
                        channel.basicAck(deliveryTag, false);  // Ack after successful save
                    } catch (IOException e) {
                        logger.error("Failed to ack message", e);
                    }
                })
                .doOnError(e -> {
                    try {
                        channel.basicReject(deliveryTag, false); // Reject without requeue on error
                    } catch (IOException ioException) {
                        logger.error("Failed to reject message", ioException);
                    }
                })
                .subscribe();

        } catch (Exception e) {
            logger.error("Failed to process message", e);
            try {
                channel.basicReject(deliveryTag, false);
            } catch (IOException ioException) {
                logger.error("Failed to reject message", ioException);
            }
        }
    }
}
