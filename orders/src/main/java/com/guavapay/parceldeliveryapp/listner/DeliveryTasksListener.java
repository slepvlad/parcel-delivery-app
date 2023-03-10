package com.guavapay.parceldeliveryapp.listner;

import com.guavapay.parceldeliveryapp.dto.DeliveryTaskEvent;
import com.guavapay.parceldeliveryapp.listner.processor.DeliveryTaskEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryTasksListener {

    private final List<DeliveryTaskEventProcessor> processors;

    @RabbitListener(queues = "delivery-task-event")
    public void listenEvent(DeliveryTaskEvent event) {
        log.info("Receive event: {}", event);
        processors.stream()
                .filter(processor -> processor.shouldProcess(event))
                .findFirst()
                .ifPresentOrElse(processor -> processor.precess(event),
                        ()-> log.warn("Not found processor for event: {}", event));
    }
}
