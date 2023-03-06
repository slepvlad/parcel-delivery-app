package com.guavapay.parceldeliveryapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateDeliveryOrderRequest {

    @NotNull
    private UUID requestId;
    @NotBlank
    private String destination;
    @NotNull
    private Long itemId;
}
