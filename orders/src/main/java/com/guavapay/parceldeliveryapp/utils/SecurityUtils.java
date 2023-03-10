package com.guavapay.parceldeliveryapp.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class SecurityUtils {

    public static Long getUserId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getCredentials();
        return (Long) jwt.getClaims().get("userId");
    }

    public static Optional<String> getTokenValue(Authentication authentication){
        return Optional.ofNullable(authentication)
                .map(auth -> (Jwt) auth.getCredentials())
                .map(AbstractOAuth2Token::getTokenValue);
    }
}
