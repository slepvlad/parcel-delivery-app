package com.guavapay.parceldeliveryapp.listner.processor;

import com.guavapay.parceldeliveryapp.dto.DeliveryTaskEvent;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import com.guavapay.parceldeliveryapp.service.DeliveryOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingForDeliveryTaskProcessor implements DeliveryTaskEventProcessor{

    private final DeliveryOrderService deliveryOrderService;

    @Override
    public boolean shouldProcess(DeliveryTaskEvent event) {
        return "WAITING".equals(event.getStatus());
    }

    @Override
    public void precess(DeliveryTaskEvent event) {
        deliveryOrderService.changeStatus(event.getOrderId(), OrderStatus.WAITING_FOR_DELIVERY);
    }
}
