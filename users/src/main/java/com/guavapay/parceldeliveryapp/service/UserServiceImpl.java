package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.UserDto;
import com.guavapay.parceldeliveryapp.exception.NotFoundException;
import com.guavapay.parceldeliveryapp.model.CustomUserDetails;
import com.guavapay.parceldeliveryapp.model.Role;
import com.guavapay.parceldeliveryapp.model.RoleName;
import com.guavapay.parceldeliveryapp.model.User;
import com.guavapay.parceldeliveryapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException(format("User with email: [%s] not found", email))
                );
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return CustomUserDetails.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public List<UserDto> findAllCouriers() {
        Role role = roleService.findByName(RoleName.ROLE_COURIER);
        return findAllByRoles(Set.of(role)).stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    private List<User> findAllByRoles(Set<Role> roles) {
        return userRepository.findAllByRolesIn(roles);
    }
}
