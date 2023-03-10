package com.guavapay.parceldeliveryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTaskEvent implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    @JsonProperty("orderId")
    private Long orderId;
    @JsonProperty("status")
    private String status;
}
