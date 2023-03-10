package com.guavapay.parceldeliveryapp.listner.processor;

import com.guavapay.parceldeliveryapp.dto.DeliveryTaskEvent;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import com.guavapay.parceldeliveryapp.service.DeliveryOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssignedTaskProcessor implements DeliveryTaskEventProcessor {

    private final DeliveryOrderService deliveryOrderService;

    @Override
    public boolean shouldProcess(DeliveryTaskEvent event) {
        return "ASSIGNED".equals(event.getStatus());
    }

    @Override
    public void precess(DeliveryTaskEvent event) {
        deliveryOrderService.changeStatus(event.getOrderId(), OrderStatus.ASSIGNED);
    }
}
