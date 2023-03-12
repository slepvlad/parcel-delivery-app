package com.guavapay.parceldeliveryapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LocationDto {

    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime createdDate;
}
