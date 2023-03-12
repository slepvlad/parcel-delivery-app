package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.*;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DeliveryOrderService {

    Long create(CreateDeliveryOrderRequest request, Authentication authentication);

    void cancel(Long orderId, Authentication authentication);

    List<DeliveryOrderDto> findAllByUser(Authentication authentication);

    void updateDestination(Long orderId, Authentication authentication, UpdateDestinationRequest request);

    void updateOrderStatus(Long orderId, UpdateStatusRequest request);

    Page<DeliveryOrderFullDto> findAll(Pageable pageable);

    List<DeliveryOrderFullDto> findAllByCourier();

    void changeStatus(Long deliveryOrderId, OrderStatus orderStatus);
}
