package com.guavapay.parceldeliveryapp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public Queue queue() {
        return new Queue("delivery-task-event", true);
    }
}
