package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.*;
import com.guavapay.parceldeliveryapp.dto.UpdateDestinationRequest;
import com.guavapay.parceldeliveryapp.service.DeliveryOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/delivery-orders")
@RequiredArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LongIdWrapper create(
            Authentication authentication,
            @Valid @RequestBody CreateDeliveryOrderRequest request
    ) {
        return new LongIdWrapper(deliveryOrderService.create(request, authentication));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @DeleteMapping("/{orderId}")
    public void cancel(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        deliveryOrderService.cancel(orderId, authentication);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/user")
    public List<DeliveryOrderDto> findAllByUser(Authentication authentication) {
        return deliveryOrderService.findAllByUser(authentication);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PutMapping("/{orderId}")
    public void update(
            @PathVariable Long orderId,
            Authentication authentication,
            @Valid @RequestBody UpdateDestinationRequest request
    ) {
        deliveryOrderService.updateDestination(orderId, authentication, request);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/{orderId}/status")
    public void changeStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        deliveryOrderService.updateOrderStatus(orderId, request);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping
    public Page<DeliveryOrderFullDto> findAll(PageRequest pageRequest) {
        return deliveryOrderService.findAll(pageRequest);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @GetMapping("/courier")
    public List<DeliveryOrderDto> findAllByCourier() {
        return deliveryOrderService.findAllByCourier();
    }
}
