package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.dto.LoginRequest;
import com.guavapay.parceldeliveryapp.dto.SignUpRequest;
import com.guavapay.parceldeliveryapp.dto.TokenDto;
import com.guavapay.parceldeliveryapp.exception.BadRequestException;
import com.guavapay.parceldeliveryapp.model.Role;
import com.guavapay.parceldeliveryapp.model.RoleName;
import com.guavapay.parceldeliveryapp.model.User;
import com.guavapay.parceldeliveryapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public Long signUpUser(SignUpRequest request) {
        return register(request, RoleName.ROLE_USER).getId();
    }

    @Override
    public Long signUpCourier(SignUpRequest request) {
        return register(request, RoleName.ROLE_COURIER).getId();
    }

    @Override
    public TokenDto signIn(LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        return new TokenDto(jwt, TOKEN_TYPE);
    }

    private User register(SignUpRequest request, RoleName roleName) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException(format("User with email: [%s] has already exist", request.getEmail()));
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        Role role = roleService.findByName(roleName);
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }
}
