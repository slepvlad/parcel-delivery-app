package com.guavapay.parceldeliveryapp.mapper;

import com.guavapay.parceldeliveryapp.dto.CreateDeliveryTaskRequest;
import com.guavapay.parceldeliveryapp.dto.DeliveryTaskShortDto;
import com.guavapay.parceldeliveryapp.model.DeliveryTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryTaskMapper {

    DeliveryTask toDeliveryTask(CreateDeliveryTaskRequest request);
    DeliveryTaskShortDto toDeliveryTaskShortDto(DeliveryTask deliveryTask);
}
