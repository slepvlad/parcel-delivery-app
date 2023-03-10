package com.guavapay.parceldeliveryapp.model;

import lombok.Getter;

@Getter
public enum DeliveryTaskStatus {

    UNASSIGNED(0),
    ASSIGNED(1),
    WAITING(2),
    ON_THE_WAY(3),
    COMPLETED(4),
    CANCELED(5);

    private final int order;

    DeliveryTaskStatus(int order) {
        this.order = order;
    }
}
