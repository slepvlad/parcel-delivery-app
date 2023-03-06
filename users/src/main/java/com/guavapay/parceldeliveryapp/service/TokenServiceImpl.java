package com.guavapay.parceldeliveryapp.service;

import com.guavapay.parceldeliveryapp.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final JwtEncoder encoder;

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        Collection<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", roles)
                .claim("userId", userPrincipal.getId())
                .claim("name", userPrincipal.getName())
                .build();
        return this.encoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
