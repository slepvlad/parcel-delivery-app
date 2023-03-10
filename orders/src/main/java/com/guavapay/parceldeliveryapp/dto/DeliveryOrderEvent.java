package com.guavapay.parceldeliveryapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 6529685098267757690L;
    private Long orderId;
    private String status;
}
