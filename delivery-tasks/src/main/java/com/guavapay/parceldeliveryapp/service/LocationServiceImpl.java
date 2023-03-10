package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.CreateLocationRequest;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.exception.BadRequestException;
import com.guavapay.parceldeliveryapp.mapper.LocationMapper;
import com.guavapay.parceldeliveryapp.repository.DeliveryTaskRepository;
import com.guavapay.parceldeliveryapp.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.guavapay.parceldeliveryapp.utils.SecurityUtils.getCourierId;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final DeliveryTaskRepository deliveryTaskRepository;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    @Override
    public LongIdWrapper create(
            Authentication authentication,
            CreateLocationRequest request
    ) {
        Long courierId = getCourierId(authentication);
        var deliveryTask = deliveryTaskRepository.findByIdAndCourierId(request.getDeliveryTaskId(), courierId)
                .orElseThrow(
                        ()-> new BadRequestException(
                                format("Not found delivery task: [%d] assigned with courier: [%d]",
                                        request.getDeliveryTaskId(),
                                        courierId)
                        )
                );
        var location = locationMapper.toLocation(request, deliveryTask);
        locationRepository.save(location);
        return new LongIdWrapper(location.getId());
    }
}
