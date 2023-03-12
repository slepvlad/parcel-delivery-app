package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.*;
import com.guavapay.parceldeliveryapp.exception.BadRequestException;
import com.guavapay.parceldeliveryapp.exception.NotFoundException;
import com.guavapay.parceldeliveryapp.mapper.DeliveryTaskMapper;
import com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus;
import com.guavapay.parceldeliveryapp.repository.DeliveryTaskRepository;
import jakarta.persistence.NamedEntityGraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus.*;
import static com.guavapay.parceldeliveryapp.utils.SecurityUtils.getCourierId;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryTaskServiceImpl implements DeliveryTaskService {

    private final DeliveryTaskRepository deliveryTaskRepository;
    private final DeliveryTaskMapper deliveryTaskMapper;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @Override
    public LongIdWrapper create(CreateDeliveryTaskRequest request) {

        if (deliveryTaskRepository
                .findByCourierIdAndOrderId(request.getCourierId(), request.getOrderId())
                .isPresent()
        ) {
            throw new BadRequestException(
                    format("Delivery task with courierId: [%s] for orderId:[%s] has already exist",
                            request.getCourierId(),
                            request.getOrderId()));
        }

        var deliveryTask = deliveryTaskMapper.toDeliveryTask(request);
        deliveryTask.setStatus(ASSIGNED);
        deliveryTaskRepository.save(deliveryTask);
        rabbitTemplate.convertAndSend(
                "delivery-task-event",
                new DeliveryTaskEvent(deliveryTask.getOrderId(), deliveryTask.getStatus().name())
        );
        return new LongIdWrapper(deliveryTask.getId());
    }

    @Override
    public List<DeliveryTaskShortDto> findAllAssigned(Authentication authentication) {

        Long courierId = getCourierId(authentication);
        return deliveryTaskRepository.findAllByCourierId(courierId).stream()
                .map(deliveryTaskMapper::toDeliveryTaskShortDto)
                .toList();
    }

    @Transactional
    @Override
    public void setStatus(
            Long deliveryTaskId,
            Authentication authentication,
            DeliveryTaskStatus status
    ) {
        Long courierId = getCourierId(authentication);
        var deliveryTask = deliveryTaskRepository.findByIdAndCourierId(deliveryTaskId, courierId)
                .orElseThrow(() ->
                        new BadRequestException(
                                format("Not found delivery task: [%d] assigned with courier: [%d]",
                                        deliveryTaskId,
                                        courierId)));

        if (status.getOrder() - 1 != deliveryTask.getStatus().getOrder()) {
            throw new BadRequestException(
                    format("Task in the wrong status: [%s]", deliveryTask.getStatus().name()));
        }

        deliveryTask.setStatus(status);
        rabbitTemplate.convertAndSend("delivery-task-event",
                new DeliveryTaskEvent(deliveryTask.getOrderId(), deliveryTask.getStatus().name()));
    }

    @Transactional
    @Override
    public void cancelDelivery(Long deliveryOrderId) {
        deliveryTaskRepository.findByOrderId(deliveryOrderId)
                .ifPresentOrElse(deliveryTask -> {
                            deliveryTask.setStatus(CANCELED);
                            deliveryTaskRepository.save(deliveryTask);
                        },
                        () -> log.warn("Not found delivery task with orderId:{} for cancelling", deliveryOrderId));
    }

    @Transactional
    @Override
    public void updateDestination(Long deliveryOrderId) {
        deliveryTaskRepository.findByOrderId(deliveryOrderId)
                .ifPresentOrElse(deliveryTask -> {
                            deliveryTask.setStatus(UNASSIGNED);
                            deliveryTask.setCourierId(null);
                            deliveryTaskRepository.save(deliveryTask);
                        },
                        () -> log.warn("Not found delivery task with orderId:{} for updating destination", deliveryOrderId));
    }

    @Override
    public DeliveryTaskFullDto getDeliveryTaskFullInfo(Long deliveryTaskId) {
        return deliveryTaskRepository.findByIdFull(deliveryTaskId)
                .map(deliveryTaskMapper::toDeliveryTaskFullDto)
                .orElseThrow(()-> new NotFoundException(format("Delivery task with id:[%d] not found", deliveryTaskId)));
    }
}
