package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.model.Role;
import com.guavapay.parceldeliveryapp.model.RoleName;
import com.guavapay.parceldeliveryapp.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import static com.guavapay.parceldeliveryapp.model.RoleName.ROLE_COURIER;
import static com.guavapay.parceldeliveryapp.model.RoleName.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SignUpCourierTest extends AbstractControllerTest {

    private static final String SIGN_UP_URL = "/api/v1/auth/sign-up-courier";
    private static final String BASE_PATH = "requests/auth/signup/courier/";

    @Test
    @DisplayName("[Success] sign up")
    @Transactional
    public void successSignUpTest() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        var token = getToken(RoleName.ROLE_ADMIN);

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_UP_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-up-courier-success.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var id = objectMapper.readValue(response, LongIdWrapper.class).getId();

        User user = userRepository.findById(id).get();
        Role userRole = roleRepository.findByName(RoleName.ROLE_COURIER)
                .orElseThrow(RuntimeException::new);

        assertEquals(id, user.getId());
        assertEquals("new user",user.getName());
        assertEquals("user-email@gmail.com", user.getEmail());
        assertNotEquals("someNewP@ssword", user.getPassword());
        assertEquals(Set.of(userRole), user.getRoles());
    }

    @DisplayName("[Fail] sign up by not an ADMIN")
    @ParameterizedTest
    @EnumSource(value = RoleName.class, names = {"ROLE_USER", "ROLE_COURIER"})
    public void failSignUpByNotAdminTest(RoleName roleName) throws Exception {

        var token = getToken(roleName);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_UP_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-up-courier-success.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] sign up Empty name")
    public void failSignUpEmptyNameTest() throws Exception {
        var token = getToken(RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_UP_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-up-courier-empty-name-fail.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] sign up Empty email")
    public void failSignUpEmptyEmailTest() throws Exception {
        var token = getToken(RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_UP_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-up-courier-empty-email-fail.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] sign up Empty password")
    public void failSignUpEmptyPasswordTest() throws Exception {
        var token = getToken(RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_UP_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-up-courier-empty-password-fail.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("[Fail] sign up Empty wrong")
    public void failSignUpWrongEmailTest() throws Exception {
        var token = getToken(RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_UP_URL)
                        .content(readFileFromResources(BASE_PATH + "sign-up-courier-wrong-email-fail.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}
