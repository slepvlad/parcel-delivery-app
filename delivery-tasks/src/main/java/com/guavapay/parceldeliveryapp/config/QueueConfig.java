package com.guavapay.parceldeliveryapp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    public static final String DELIVERY_ORDER_EVENT = "delivery-order-event";

    @Bean
    public Queue queue() {
        return new Queue(DELIVERY_ORDER_EVENT, true);
    }
}
