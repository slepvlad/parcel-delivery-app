package com.guavapay.parceldeliveryapp.repository;

import com.guavapay.parceldeliveryapp.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

    Optional<DeliveryOrder> findByUserIdAndRequestId(Long userId, UUID requestId);
    Optional<DeliveryOrder> findByIdAndUserId(Long orderId, Long userId);

    List<DeliveryOrder> findAllByUserId(Long userId);
    List<DeliveryOrder> findAllByIdIn(List<Long> isd);
}
