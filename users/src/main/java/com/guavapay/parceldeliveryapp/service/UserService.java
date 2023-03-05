package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAllCouriers();
}
