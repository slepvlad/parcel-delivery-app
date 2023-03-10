package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.client.DeliveryTaskClient;
import com.guavapay.parceldeliveryapp.client.dto.AssignedOrderDto;
import com.guavapay.parceldeliveryapp.dto.*;
import com.guavapay.parceldeliveryapp.exception.BadRequestException;
import com.guavapay.parceldeliveryapp.exception.NotFoundException;
import com.guavapay.parceldeliveryapp.mapper.DeliveryOrderMapper;
import com.guavapay.parceldeliveryapp.model.DeliveryOrder;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import com.guavapay.parceldeliveryapp.repository.DeliveryOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.guavapay.parceldeliveryapp.utils.SecurityUtils.getUserId;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryOrderMapper deliveryOrderMapper;
    private final DeliveryTaskClient deliveryTaskClient;
    private final RabbitTemplate rabbitTemplate;

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
        if(!OrderStatus.cancelable().contains(order.getOrderStatus())){
            throw new BadRequestException(
                    format("Order with status: [%s] can't be cancelled", order.getOrderStatus().name()));
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        deliveryOrderRepository.save(order);
        rabbitTemplate
                .convertAndSend(
                        "delivery-order-event",
                        new DeliveryOrderEvent(orderId, "CANCELED")
                );
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

        if(!OrderStatus.updatable().contains(order.getOrderStatus())){
            throw new BadRequestException(
                    format("Order with status: [%s] can't be updated", order.getOrderStatus().name()));
        }
        order.setDestination(request.getDestination());
        deliveryOrderRepository.save(order);
        rabbitTemplate.convertAndSend("delivery-order-event",
                new DeliveryOrderEvent(orderId, "UPDATE_DESTINATION")
        );
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, UpdateStatusRequest request) {
        var order = deliveryOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(format("Not found order with orderId:[%s]", orderId)));

        order.setOrderStatus(request.getStatus());
        deliveryOrderRepository.save(order);
    }

    @Override
    public Page<DeliveryOrderFullDto> findAll(PageRequest pageRequest) {
        return deliveryOrderRepository.findAll(pageRequest)
                .map(deliveryOrderMapper::toDeliveryOrderFullDto);
    }

    @Override
    public List<DeliveryOrderDto> findAllByCourier() {
        var orderIds = deliveryTaskClient.findAllAssigned().stream()
                .map(AssignedOrderDto::getOrderId)
                .toList();

        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }

        return deliveryOrderRepository.findAllByIdIn(orderIds).stream()
                .map(deliveryOrderMapper::toDeliveryOrderDto)
                .toList();
    }

    @Override
    public void changeStatus(Long deliveryOrderId, OrderStatus orderStatus) {
        deliveryOrderRepository.findById(deliveryOrderId)
                .ifPresentOrElse(
                        order -> {
                            order.setOrderStatus(orderStatus);
                            deliveryOrderRepository.save(order);
                        },
                        () -> log.error("Not found delivery order with id: {}", deliveryOrderId)
                );
    }

    private DeliveryOrder findByOrderIdAndUserId(Long orderId, Long userId) {
        return deliveryOrderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NotFoundException(format("Not found order with orderId:[%s]", orderId)));
    }
}
