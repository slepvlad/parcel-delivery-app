package com.guavapay.parceldeliveryapp.listner.processor;

import com.guavapay.parceldeliveryapp.dto.DeliveryOrderEvent;

public interface DeliveryOrderEventProcessor {

    boolean shouldProcess(DeliveryOrderEvent event);

    void precess(DeliveryOrderEvent event);
}
