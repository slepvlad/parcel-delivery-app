package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.SignUpRequest;
import com.guavapay.parceldeliveryapp.dto.TokenDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Long signUpUser(SignUpRequest request);

    Long signUpCourier(SignUpRequest request);

    TokenDto signIn(Authentication authentication);
}
