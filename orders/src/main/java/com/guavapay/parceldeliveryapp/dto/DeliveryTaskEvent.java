package com.guavapay.parceldeliveryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class DeliveryTaskEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757690L;
    private Long orderId;
    private String status;
}
