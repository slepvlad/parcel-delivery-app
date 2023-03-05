package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.model.Role;
import com.guavapay.parceldeliveryapp.model.RoleName;
import com.guavapay.parceldeliveryapp.model.User;
import com.guavapay.parceldeliveryapp.repository.RoleRepository;
import com.guavapay.parceldeliveryapp.repository.UserRepository;
import com.guavapay.parceldeliveryapp.service.JwtTokenProvider;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {

    protected final String AUTH_HEADER = "Authorization";
    protected final String TOKEN_TYPE = "Bearer ";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RoleRepository roleRepository;

    private static PostgreSQLContainer<?> postgres;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected AuthenticationManager authenticationManager;

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

    @BeforeEach
    public void cleanDbFromLiquibase(){
        userRepository.deleteAll();
    }

    @AfterEach
    public void cleanDb() {
        userRepository.deleteAll();
    }

    protected User createUser(RoleName roleName) {
        return createUser("user@mail.com", roleName);
    }

    protected User createUser(String email, RoleName roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(RuntimeException::new);

        User user = new User();
        user.setName("user");
        user.setEmail(email);
        user.setPassword("$2a$12$Ax2sFo7hFc5TJY.U2K5joe66RljhJ6SD6FDAjFggqTfTa4UU7OkVy");
        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }

    protected String getToken(RoleName roleName) {
        createUser(roleName);
        Authentication authentication = getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return TOKEN_TYPE + jwtTokenProvider.generateToken(authentication);
    }

    protected Authentication getAuthentication() {
        return authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken("user@mail.com", "P@ssw0rd")
                );
    }

    protected String readFileFromResources(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return FileUtils.readFileToString(file, "UTF-8");
    }
}
