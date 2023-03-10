package com.guavapay.parceldeliveryapp.repository;

import com.guavapay.parceldeliveryapp.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
