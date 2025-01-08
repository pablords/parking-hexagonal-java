package com.pablords.parking.adapters.outbound.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "checkoutQueue";
    public static final String EXCHANGE_NAME = "checkoutExchange";

    @Bean
    public Queue checkoutQueue() {
        return new Queue(QUEUE_NAME, true); // true para fila persistente
    }

    @Bean
    public DirectExchange checkoutExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue checkoutQueue, DirectExchange checkoutExchange) {
        return BindingBuilder.bind(checkoutQueue).to(checkoutExchange).with("checkoutRoutingKey");
    }
}
