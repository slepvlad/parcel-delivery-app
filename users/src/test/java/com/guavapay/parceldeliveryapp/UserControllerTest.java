package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.model.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractControllerTest {

    private static final String FIND_ALL_COURIERS_URL = "/api/v1/users/couriers";

    @Test
    @DisplayName("[Success] get all couriers by admin")
    public void successGetAllByAdminTest() throws Exception {

        createUser("courier1@gmail.com", RoleName.ROLE_COURIER);
        createUser("courier2@gmail.com", RoleName.ROLE_COURIER);

        String token = getToken(RoleName.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_COURIERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("user"))
                .andExpect(jsonPath("$[0].email").value("courier1@gmail.com"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").value("user"))
                .andExpect(jsonPath("$[1].email").value("courier2@gmail.com"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] get all couriers by user")
    public void successGetAllByUserTest() throws Exception {

        createUser("courier1@gmail.com", RoleName.ROLE_COURIER);
        createUser("courier2@gmail.com", RoleName.ROLE_COURIER);

        String token = getToken(RoleName.ROLE_USER);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_COURIERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[Fail] get all couriers by courier")
    public void successGetAllByCourierTest() throws Exception {

        createUser("courier1@gmail.com", RoleName.ROLE_COURIER);
        createUser("courier2@gmail.com", RoleName.ROLE_COURIER);

        String token = getToken(RoleName.ROLE_COURIER);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_COURIERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, token))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }
}
