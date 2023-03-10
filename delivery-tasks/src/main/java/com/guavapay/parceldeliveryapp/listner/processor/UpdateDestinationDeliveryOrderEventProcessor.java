package com.guavapay.parceldeliveryapp.listner.processor;

import com.guavapay.parceldeliveryapp.dto.DeliveryOrderEvent;
import com.guavapay.parceldeliveryapp.service.DeliveryTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateDestinationDeliveryOrderEventProcessor implements DeliveryOrderEventProcessor{

    private final DeliveryTaskService deliveryTaskService;

    @Override
    public boolean shouldProcess(DeliveryOrderEvent event) {
        return "UPDATE_DESTINATION".equals(event.getStatus());
    }

    @Override
    public void precess(DeliveryOrderEvent event) {
        log.info("Receive event:{}", event);
        deliveryTaskService.updateDestination(event.getOrderId());
    }
}
