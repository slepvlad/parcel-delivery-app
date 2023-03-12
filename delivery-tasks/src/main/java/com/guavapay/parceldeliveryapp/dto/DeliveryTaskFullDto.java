package com.guavapay.parceldeliveryapp.dto;

import com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeliveryTaskFullDto {
    private Long id;
    private Long courierId;
    private Long orderId;
    private DeliveryTaskStatus status;
    private List<LocationDto> locations;
}
