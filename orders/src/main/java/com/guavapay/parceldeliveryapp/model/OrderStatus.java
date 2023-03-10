package com.guavapay.parceldeliveryapp.model;

import java.util.Set;

public enum OrderStatus {

    NEW,
    ASSIGNED,
    WAITING_FOR_DELIVERY,
    ON_THE_WAY,
    CANCELED,
    DELIVERED;

    public static Set<OrderStatus> cancelable(){
        return Set.of(NEW, ASSIGNED, WAITING_FOR_DELIVERY);
    }

    public static Set<OrderStatus> updatable(){
        return Set.of(NEW, ASSIGNED);
    }
}
