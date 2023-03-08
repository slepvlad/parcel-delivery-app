package com.guavapay.parceldeliveryapp.dto;

import com.guavapay.parceldeliveryapp.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {

    @NotNull
    private OrderStatus status;
}
