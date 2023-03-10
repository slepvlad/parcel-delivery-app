package com.guavapay.parceldeliveryapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDeliveryTaskRequest {

    @NotNull
    private Long courierId;
    @NotNull
    private Long orderId;
}
