package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.model.Role;
import com.guavapay.parceldeliveryapp.model.RoleName;

public interface RoleService {

    Role findByName(RoleName roleName);
}
