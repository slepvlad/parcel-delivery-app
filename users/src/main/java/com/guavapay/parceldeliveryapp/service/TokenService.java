package com.guavapay.parceldeliveryapp.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String generateToken(Authentication authentication);
}
