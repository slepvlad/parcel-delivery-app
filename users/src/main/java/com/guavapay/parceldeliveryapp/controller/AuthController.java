package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.dto.SignUpRequest;
import com.guavapay.parceldeliveryapp.dto.TokenDto;
import com.guavapay.parceldeliveryapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/sign-up-user")
    public LongIdWrapper signUpUser(@Valid @RequestBody SignUpRequest request) {
        return new LongIdWrapper(authService.signUpUser(request));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping(value = "/sign-up-courier")
    public LongIdWrapper signUpCourier(@Valid @RequestBody SignUpRequest request) {
        return new LongIdWrapper(authService.signUpCourier(request));
    }

    @PostMapping("/sign-in")
    public TokenDto signIn(Authentication authentication) {
        Assert.notNull(authentication, "Unauthorized. Wrong username or password");
        return authService.signIn(authentication);
    }
}
