package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.dto.SignUpRequest;
import com.guavapay.parceldeliveryapp.dto.TokenDto;
import com.guavapay.parceldeliveryapp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication controller", description = "sign up an sign in users")
public class AuthController {

    private final AuthService authService;


    @Operation(
            summary = "[User stories: User] Can create an user account",
            description = "Create new user by user"
    )
    @PostMapping(value = "/sign-up-user", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public LongIdWrapper signUpUser(@Valid @RequestBody SignUpRequest request) {
        return new LongIdWrapper(authService.signUpUser(request));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping(value = "/sign-up-courier")
    @Operation(
            summary = "[User stories: Admin] Can create an courier account",
            description = "Create new courier by admin"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public LongIdWrapper signUpCourier(@Valid @RequestBody SignUpRequest request) {
        return new LongIdWrapper(authService.signUpCourier(request));
    }

    @PostMapping("/sign-in")
    @SecurityRequirement(name = "basicAuth")
    @Operation(
            summary = "[User stories: Admin, User, Courier] Can get JWT for access",
            description = "Get JWT for login and api access"
    )
    public TokenDto signIn(Authentication authentication) {
        log.info("Request: Hi");
        Assert.notNull(authentication, "Unauthorized. Wrong username or password");
        return authService.signIn(authentication);
    }
}
