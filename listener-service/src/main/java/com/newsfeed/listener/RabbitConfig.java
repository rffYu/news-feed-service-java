package com.newsfeed.listener;

import com.newsfeed.listener.constants.MQConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Jackson2JsonMessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
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
}
