package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.repository.DeliveryOrderRepository;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {

    private final static String TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsInNjb3BlIjpbIlJPTEVfVVNFUiJdLCJpc3MiOiJzZWxmIiwibmFtZSI6InVzZXIiLCJleHAiOjE2NzgxODg1NTYsImlhdCI6MTY3ODE4NDk1NiwidXNlcklkIjoxfQ.fF2MAaRCM6CKZFZMfn4dT6bLSCddZKidyo-ChmZk9xRopdbXskYhF84qhC-Z6OuH6jv1HSJOe3lSW9H2vNnSI8GoDEzsDpai-wPHXyto3nEfW6zDK2qLTLysD71tnz9AC4B6dm5EjbQ1n8GjHbBElSbazo_C_eiNZoVOtc1B_OVwwHuXBFCpMrz0M-gK_Eh4xEYwXceMYTF-QJ8krvu8mOO18-w2AvGiR9VFJyuR8mjAK1cQMuxSaqYmCx0CB7Fm7E34TbML_khqSxHZzwA1CcGd-yLR-cay3EvYaBNA9cy_40yb5U_oj5VAbbKy-hROfeXu_PSYJ_JIR0OClyM9lA";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected DeliveryOrderRepository deliveryOrderRepository;

    private static PostgreSQLContainer<?> postgres;

    @BeforeAll
    static void init() {
        postgres = new PostgreSQLContainer<>("postgres").withReuse(true);
        postgres.start();
    }

    @DynamicPropertySource
    public static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @AfterEach
    public void cleanDb() {
        deliveryOrderRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    protected void authentication(Long userId, String role) {
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("SCOPE_" + role));

        Instant now = Instant.now();
        Instant expired = now.plus(1, ChronoUnit.HOURS);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", userId);

        OAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(attributes, authorities);
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "user@gmail.com");
        claims.put("scope", Set.of("ROLE_USER"));
        claims.put("iss", "self");
        claims.put("name", "user");
        claims.put("exp", expired.getEpochSecond());
        claims.put("iat", now.getEpochSecond());
        claims.put("userId", userId);

        Jwt jwt = Jwt.withTokenValue(TOKEN)
                .headers((h) -> h.putAll(headers))
                .claims((c) -> c.putAll(claims))
                .issuedAt(now)
                .expiresAt(expired)
                .build();

        Authentication authentication = new BearerJwtTokenTestAuthentication(principal, jwt, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected String readFileFromResources(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return FileUtils.readFileToString(file, "UTF-8");
    }

}
