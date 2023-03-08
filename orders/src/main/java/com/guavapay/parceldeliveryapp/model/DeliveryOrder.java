package com.guavapay.parceldeliveryapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID requestId;
    private Long userId;
    private String destination;
    private Long itemId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


}
