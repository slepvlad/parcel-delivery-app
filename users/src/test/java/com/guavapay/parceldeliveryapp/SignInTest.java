package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.model.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SignInTest extends AbstractControllerTest {

    private static final String SIGN_IN_URL = "/api/v1/auth/sign-in";

    @Test
    @DisplayName("[Success] sign in by admin")
    public void successSignInByAdminTest() throws Exception {

        createUser("admin@gmail.com", RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .header(AUTH_HEADER, getBasicAuth("admin@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Success] sign in by user")
    public void successSignInByUserTest() throws Exception {

        createUser("user@gmail.com", RoleName.ROLE_USER);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .header(AUTH_HEADER, getBasicAuth("user@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Success] sign in by courier")
    public void successSignInByCourierTest() throws Exception {

        createUser("courier@gmail.com", RoleName.ROLE_COURIER);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .header(AUTH_HEADER, getBasicAuth("courier@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] sign in by admin Wrong password")
    public void failSignInByAdminTest() throws Exception {

        createUser("admin@gmail.com", RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .header(AUTH_HEADER, getBasicAuth("admin@gmail.com", "blabla"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.accessToken").doesNotExist())
                .andExpect(jsonPath("$.type").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] sign in by admin Wrong email")
    public void failSignInWrongEmailTest() throws Exception {

        createUser("admin@gmail.com", RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .header(AUTH_HEADER, getBasicAuth("admin!@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

}
