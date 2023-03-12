package com.guavapay.parceldeliveryapp.mapper;

import com.guavapay.parceldeliveryapp.dto.CreateDeliveryTaskRequest;
import com.guavapay.parceldeliveryapp.dto.DeliveryTaskFullDto;
import com.guavapay.parceldeliveryapp.dto.DeliveryTaskShortDto;
import com.guavapay.parceldeliveryapp.model.DeliveryTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DeliveryTaskMapper {

    DeliveryTask toDeliveryTask(CreateDeliveryTaskRequest request);
    DeliveryTaskShortDto toDeliveryTaskShortDto(DeliveryTask deliveryTask);

    DeliveryTaskFullDto toDeliveryTaskFullDto(DeliveryTask deliveryTask);
}
