package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.CreateLocationRequest;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_COURIER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LongIdWrapper create(
            Authentication authentication,
            @Valid @RequestBody CreateLocationRequest request
    ) {
        return locationService.create(authentication, request);
    }
}
