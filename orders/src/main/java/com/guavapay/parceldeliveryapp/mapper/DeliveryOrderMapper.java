package com.guavapay.parceldeliveryapp.mapper;

import com.guavapay.parceldeliveryapp.dto.DeliveryOrderDto;
import com.guavapay.parceldeliveryapp.dto.DeliveryOrderFullDto;
import com.guavapay.parceldeliveryapp.model.DeliveryOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryOrderMapper {

    DeliveryOrderDto toDeliveryOrderDto(DeliveryOrder deliveryOrder);

    DeliveryOrderFullDto toDeliveryOrderFullDto(DeliveryOrder deliveryOrder);
}
