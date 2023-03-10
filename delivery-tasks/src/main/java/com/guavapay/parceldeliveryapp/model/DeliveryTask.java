package com.guavapay.parceldeliveryapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "delivery_tasks")
@EntityListeners(AuditingEntityListener.class)
public class DeliveryTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courierId;
    private Long orderId;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    @Enumerated(EnumType.STRING)
    private DeliveryTaskStatus status;
    @OneToMany(mappedBy = "deliveryTask")
    private List<Location> locations = new ArrayList<>();

}
