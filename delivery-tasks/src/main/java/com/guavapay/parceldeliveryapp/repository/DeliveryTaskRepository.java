package com.guavapay.parceldeliveryapp.repository;

import com.guavapay.parceldeliveryapp.model.DeliveryTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryTaskRepository extends JpaRepository<DeliveryTask, Long> {

    Optional<DeliveryTask> findByCourierIdAndOrderId(Long courierId, Long orderId);

    Optional<DeliveryTask> findByIdAndCourierId(Long id, Long courierId);

    List<DeliveryTask> findAllByCourierId(Long courierId);

    Optional<DeliveryTask> findByOrderId(Long orderId);
}
