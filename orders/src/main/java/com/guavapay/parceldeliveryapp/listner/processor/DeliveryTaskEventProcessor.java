package com.guavapay.parceldeliveryapp.listner.processor;

import com.guavapay.parceldeliveryapp.dto.DeliveryTaskEvent;

public interface DeliveryTaskEventProcessor {

    boolean shouldProcess(DeliveryTaskEvent event);

    void precess(DeliveryTaskEvent event);

}
