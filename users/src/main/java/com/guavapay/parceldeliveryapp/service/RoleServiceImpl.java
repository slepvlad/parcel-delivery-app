package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.exception.InternalAppException;
import com.guavapay.parceldeliveryapp.model.Role;
import com.guavapay.parceldeliveryapp.model.RoleName;
import com.guavapay.parceldeliveryapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public Role findByName(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new InternalAppException("Not found role in db. Add role to database"));
    }
}
