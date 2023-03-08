package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.CreateDeliveryOrderRequest;
import com.guavapay.parceldeliveryapp.dto.DeliveryOrderDto;
import com.guavapay.parceldeliveryapp.dto.DeliveryOrderFullDto;
import com.guavapay.parceldeliveryapp.dto.UpdateStatusRequest;
import com.guavapay.parceldeliveryapp.exception.BadRequestException;
import com.guavapay.parceldeliveryapp.exception.NotFoundException;
import com.guavapay.parceldeliveryapp.mapper.DeliveryOrderMapper;
import com.guavapay.parceldeliveryapp.model.DeliveryOrder;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import com.guavapay.parceldeliveryapp.dto.UpdateDestinationRequest;
import com.guavapay.parceldeliveryapp.repository.DeliveryOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryOrderMapper deliveryOrderMapper;

    @Override
    @Transactional
    public Long create(CreateDeliveryOrderRequest request, Authentication authentication) {
        Long userId = getUserId(authentication);
        if (deliveryOrderRepository.findByUserIdAndRequestId(userId, request.getRequestId()).isPresent()) {
            throw new BadRequestException(
                    format("Order with user: [%d] and [%s] has already exist",
                            userId,
                            request.getRequestId()
                    ));
        }

        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setRequestId(request.getRequestId());
        deliveryOrder.setOrderStatus(OrderStatus.NEW);
        deliveryOrder.setUserId(userId);
        deliveryOrder.setItemId(request.getItemId());
        deliveryOrder.setDestination(request.getDestination());

        deliveryOrderRepository.save(deliveryOrder);

        return deliveryOrder.getId();
    }

    @Override
    @Transactional
    public void cancel(Long orderId, Authentication authentication) {
        Long userId = getUserId(authentication);
        var order = findByOrderIdAndUserId(orderId, userId);
        //ToDo check current status
        order.setOrderStatus(OrderStatus.CANCELED);
        deliveryOrderRepository.save(order);
    }

    @Override
    public List<DeliveryOrderDto> findAllByUser(Authentication authentication) {
        Long userId = getUserId(authentication);
        return deliveryOrderRepository.findAllByUserId(userId).stream()
                .map(deliveryOrderMapper::toDeliveryOrderDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateDestination(Long orderId, Authentication authentication, UpdateDestinationRequest request) {
        Long userId = getUserId(authentication);
        var order = findByOrderIdAndUserId(orderId, userId);
        //ToDo check status

        order.setDestination(request.getDestination());
        deliveryOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, UpdateStatusRequest request) {
        var order = deliveryOrderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(format("Not found order with orderId:[%s]", orderId)));

        order.setOrderStatus(request.getStatus());
        deliveryOrderRepository.save(order);
    }

    @Override
    public Page<DeliveryOrderFullDto> findAll(PageRequest pageRequest) {
        return deliveryOrderRepository.findAll(pageRequest)
                .map(deliveryOrderMapper::toDeliveryOrderFullDto);
    }

    private Long getUserId(Authentication authentication) {
        Object credentials = authentication.getCredentials();
        Jwt jwt = (Jwt) authentication.getCredentials();
        return (Long) jwt.getClaims().get("userId");
    }

    private DeliveryOrder findByOrderIdAndUserId(Long orderId, Long userId) {
        return deliveryOrderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NotFoundException(format("Not found order with orderId:[%s]", orderId)));
    }
}
