package com.guavapay.parceldeliveryapp;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Transient
public class BearerJwtTokenTestAuthentication extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Map<String, Object> attributes;

    public BearerJwtTokenTestAuthentication(OAuth2AuthenticatedPrincipal principal, Jwt credentials,
                                            Collection<? extends GrantedAuthority> authorities) {
        super(credentials, principal, credentials, authorities);
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(principal.getAttributes()));
        setAuthenticated(true);
    }

    @Override
    public Map<String, Object> getTokenAttributes() {
        return this.attributes;
    }
}
