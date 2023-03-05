package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.LoginRequest;
import com.guavapay.parceldeliveryapp.dto.SignUpRequest;
import com.guavapay.parceldeliveryapp.dto.TokenDto;

public interface AuthService {
    Long signUpUser(SignUpRequest request);

    Long signUpCourier(SignUpRequest request);

    TokenDto signIn(LoginRequest request);
}
