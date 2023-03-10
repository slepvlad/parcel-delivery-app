package com.guavapay.parceldeliveryapp.listner;

import com.guavapay.parceldeliveryapp.config.QueueConfig;
import com.guavapay.parceldeliveryapp.dto.DeliveryOrderEvent;
import com.guavapay.parceldeliveryapp.listner.processor.DeliveryOrderEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryOrderListener {

    private final List<DeliveryOrderEventProcessor> processors;

    @RabbitListener(queues = QueueConfig.DELIVERY_ORDER_EVENT)
    public void listenEvent(DeliveryOrderEvent event) {

        processors.stream()
                .filter(processor -> processor.shouldProcess(event))
                .findFirst()
                .ifPresentOrElse(processors->processors.precess(event),
                        ()->log.warn("Not found processor for event: {}", event));
    }
}
