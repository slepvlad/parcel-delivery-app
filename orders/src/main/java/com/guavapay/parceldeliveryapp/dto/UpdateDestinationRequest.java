package com.guavapay.parceldeliveryapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDestinationRequest {

    @NotBlank
    private String destination;
}
