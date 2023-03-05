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
    private static final String BASE_PATH = "requests/auth/signin/";

    @Test
    @DisplayName("[Success] sign in by admin")
    public void successSignInByAdminTest() throws Exception {

        createUser("admin@gmail.com", RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-in-admin-success.json"))
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
                        .content(readFileFromResources(BASE_PATH + "sign-in-user-success.json"))
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
                        .content(readFileFromResources(BASE_PATH + "sign-in-courier-success.json"))
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
                        .content(readFileFromResources(BASE_PATH + "sign-in-admin-password-fail.json"))
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
                        .content(readFileFromResources(BASE_PATH + "sign-in-admin-email-fail.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("User with email: [admin!@gmail.com] not found"))
                .andExpect(jsonPath("$.instance").value("/api/v1/auth/sign-in"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] bad request sign in by admin Wrong email")
    public void badRequestSignInWrongEmailTest() throws Exception {

        createUser("admin@gmail.com", RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-in-admin-email-bad-request.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] bad request sign in by admin Wrong password")
    public void badRequestSignInWrongPasswordTest() throws Exception {

        createUser("admin@gmail.com", RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_IN_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-in-admin-password-bad-request.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}
