package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.CreateLocationRequest;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import org.springframework.security.core.Authentication;

public interface LocationService {

    LongIdWrapper create(Authentication authentication, CreateLocationRequest request);
}
