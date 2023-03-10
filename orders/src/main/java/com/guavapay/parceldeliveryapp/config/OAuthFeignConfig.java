package com.guavapay.parceldeliveryapp.config;

import com.guavapay.parceldeliveryapp.utils.SecurityUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class OAuthFeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var optionalToken = SecurityUtils.getTokenValue(authentication);
        optionalToken
                .map(token -> requestTemplate.header("Authorization", "Bearer " + token));
    }
}
