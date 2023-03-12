package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.*;
import com.guavapay.parceldeliveryapp.service.DeliveryOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/delivery-orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Delivery order controller",
        description = "Controller for create update and cancel orders"
)
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "[User stories: User] Can create a parcel delivery order",
            description = "Create new parcel delivery order"
    )
    public LongIdWrapper create(
            Authentication authentication,
            @Valid @RequestBody CreateDeliveryOrderRequest request
    ) {
        return new LongIdWrapper(deliveryOrderService.create(request, authentication));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/{orderId}/cancel")
    @Operation(
            summary = "[User stories: User] Can cancel a parcel delivery order",
            description = "Cancel existed parcel delivery order.\n" +
                    " Allow operation if order in cancellable status: [NEW, ASSIGNED, WAITING_FOR_DELIVERY]"
    )
    public void cancel(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        deliveryOrderService.cancel(orderId, authentication);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/user")
    @Operation(
            summary = "[User stories: User] Can see all parcel delivery orders that he/she created",
            description = "Return list of parcel delivery orders"
    )
    public List<DeliveryOrderDto> findAllByUser(Authentication authentication) {
        return deliveryOrderService.findAllByUser(authentication);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PutMapping("/{orderId}")
    @Operation(
            summary = "[User stories: User] Can change the destination of a parcel delivery order",
            description = "Update destination\n" +
                    "Allow operation if order in updatable status: [NEW, ASSIGNED]"
    )
    public void update(
            @PathVariable Long orderId,
            Authentication authentication,
            @Valid @RequestBody UpdateDestinationRequest request
    ) {
        deliveryOrderService.updateDestination(orderId, authentication, request);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/{orderId}/status")
    @Operation(
            summary = "[User stories: Admin] Can change the status of a parcel delivery order",
            description = "Change the status a parcel delivery order"
    )
    public void changeStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        deliveryOrderService.updateOrderStatus(orderId, request);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping
    @Operation(
            summary = "[User stories: Admin] Can view all parcel delivery orders",
            description = "Return all all parcel delivery orders"
    )
    public Page<DeliveryOrderFullDto> findAll(@ParameterObject Pageable pageable) {
        return deliveryOrderService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @GetMapping("/courier")
    @Operation(
            summary = "[User stories: Courier] Can view all parcel delivery orders that assigned to him",
            description = "Return all all parcel delivery orders that assigned to him"
    )
    public List<DeliveryOrderFullDto> findAllByCourier() {
        return deliveryOrderService.findAllByCourier();
    }

    //ToDo Can see the details of a delivery;
}
