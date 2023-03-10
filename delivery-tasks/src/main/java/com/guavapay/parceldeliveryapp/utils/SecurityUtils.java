package com.guavapay.parceldeliveryapp.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public class SecurityUtils {

    public static Long getCourierId(Authentication authentication) {
        Assert.notNull(authentication, "Sorry, you should authorize");
        Jwt jwt = (Jwt) authentication.getCredentials();
        return (Long) jwt.getClaims().get("userId");
    }
}
