package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.CreateDeliveryTaskRequest;
import com.guavapay.parceldeliveryapp.dto.DeliveryTaskShortDto;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus;
import com.guavapay.parceldeliveryapp.service.DeliveryTaskService;
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
public class DeliveryTaskController {

    private final DeliveryTaskService deliveryTaskService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LongIdWrapper create(
            @Valid @RequestBody CreateDeliveryTaskRequest request
    ) {
        return deliveryTaskService.create(request);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @GetMapping("/courier")
    public List<DeliveryTaskShortDto> findAllAssigned(Authentication authentication) {
        return deliveryTaskService.findAllAssigned(authentication);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @PostMapping("/{deliveryTaskId}/status/{status}")
    public void setStatus(
            @PathVariable Long deliveryTaskId,
            @PathVariable DeliveryTaskStatus status,
            Authentication authentication
    ) {
        deliveryTaskService.setStatus(deliveryTaskId, authentication, status);
    }
}
