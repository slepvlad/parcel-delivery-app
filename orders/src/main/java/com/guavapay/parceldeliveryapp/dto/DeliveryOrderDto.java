package com.guavapay.parceldeliveryapp.dto;

import com.guavapay.parceldeliveryapp.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryOrderDto {

    private Long id;
    private String destination;
    private Long itemId;
    private OrderStatus orderStatus;
}
