package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.CreateDeliveryTaskRequest;
import com.guavapay.parceldeliveryapp.dto.DeliveryTaskShortDto;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus;
import com.guavapay.parceldeliveryapp.service.DeliveryTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/delivery-tasks")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Delivery task controller",
        description = "Controller for operate with delivery task for parcel delivery order"
)
public class DeliveryTaskController {

    private final DeliveryTaskService deliveryTaskService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "[User stories: Admin] Can assign parcel delivery order to courier",
            description = "Assigning existed parcel delivery order to courier by creating delivery task"
    )
    public LongIdWrapper create(
            @Valid @RequestBody CreateDeliveryTaskRequest request
    ) {
        return deliveryTaskService.create(request);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @GetMapping("/courier")
    @Operation(
            summary = "Return all delivery task on the courier",
            description = "Return all delivery task on the courier"
    )
    public List<DeliveryTaskShortDto> findAllAssigned(Authentication authentication) {
        return deliveryTaskService.findAllAssigned(authentication);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @PostMapping("/{deliveryTaskId}/status/{status}")
    @Operation(
            summary = "[User stories: Courier] Can change the status of a parcel delivery order",
            description = "Change the status of a parcel delivery order by changing status the delivery task"
    )
    public void setStatus(
            @PathVariable Long deliveryTaskId,
            @PathVariable DeliveryTaskStatus status,
            Authentication authentication
    ) {
        deliveryTaskService.setStatus(deliveryTaskId, authentication, status);
    }

    //ToDo Can see the details of a delivery;
}
