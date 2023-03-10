package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.CreateLocationRequest;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Location controller",
        description = "Controller for receiving delivery location from courier device (or others devises)"
)
public class LocationController {

    private final LocationService locationService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Store delivery location",
            description = "Store delivery locations for tracking delivery"
    )
    public LongIdWrapper create(
            Authentication authentication,
            @Valid @RequestBody CreateLocationRequest request
    ) {
        return locationService.create(authentication, request);
    }
}
