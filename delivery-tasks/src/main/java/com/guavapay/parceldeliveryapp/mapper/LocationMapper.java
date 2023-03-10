package com.guavapay.parceldeliveryapp.mapper;


import com.guavapay.parceldeliveryapp.dto.CreateLocationRequest;
import com.guavapay.parceldeliveryapp.model.DeliveryTask;
import com.guavapay.parceldeliveryapp.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mappings({
            @Mapping(target = "latitude", source = "request.latitude"),
            @Mapping(target = "longitude", source = "request.longitude"),
            @Mapping(target = "deliveryTask", source = "deliveryTask"),
    })
    Location toLocation(CreateLocationRequest request, DeliveryTask deliveryTask);
}
