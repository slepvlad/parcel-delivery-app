package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.CreateDeliveryTaskRequest;
import com.guavapay.parceldeliveryapp.dto.DeliveryTaskShortDto;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DeliveryTaskService {
    LongIdWrapper create(CreateDeliveryTaskRequest request);

    List<DeliveryTaskShortDto> findAllAssigned(Authentication authentication);

    void setStatus(Long deliveryTaskId, Authentication authentication, DeliveryTaskStatus status);

    void cancelDelivery(Long deliveryOrderId);

    void updateDestination(Long orderId);
}
