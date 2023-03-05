package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.LoginRequest;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.dto.SignUpRequest;
import com.guavapay.parceldeliveryapp.dto.TokenDto;
import com.guavapay.parceldeliveryapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/sign-up-user")
    public LongIdWrapper signUpUser(@Valid @RequestBody SignUpRequest request) {
        return new LongIdWrapper(authService.signUpUser(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/sign-up-courier")
    public LongIdWrapper signUpCourier(@Valid @RequestBody SignUpRequest request) {
        return new LongIdWrapper(authService.signUpCourier(request));
    }

    @PostMapping("/sign-in")
    public TokenDto signIn(@Valid @RequestBody LoginRequest request) {
        return authService.signIn(request);
    }
}
