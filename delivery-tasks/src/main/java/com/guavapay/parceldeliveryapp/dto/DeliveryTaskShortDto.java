package com.guavapay.parceldeliveryapp.dto;

import com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryTaskShortDto {
    private Long id;
    private Long courierId;
    private Long orderId;
    private DeliveryTaskStatus status;
}
