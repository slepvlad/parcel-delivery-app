package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.CreateLocationRequest;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateLocationTest extends AbstractControllerTest {

    private final static String CREATE_LOCATION_URL = "/api/v1/locations";
    private static final String BASE_PATH = "requests/location/create/";

    @Test
    @DisplayName("[Success] Create location by courier")
    @Transactional
    public void createLocationTest() throws Exception {

        authentication(1L, "ROLE_COURIER");
        var task = createDeliveryTask();

        var request = new CreateLocationRequest();
        request.setLatitude(new BigDecimal("51.502109480852155"));
        request.setLongitude(new BigDecimal("-0.13788590216932878"));
        request.setDeliveryTaskId(task.getId());

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_LOCATION_URL)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = mapper.readValue(response, LongIdWrapper.class).getId();

        var location = locationRepository.findById(id);

        assertTrue(location.isPresent());
        assertEquals(id, location.get().getId());
        assertEquals(new BigDecimal("51.502109480852155"), location.get().getLatitude());
        assertEquals(new BigDecimal("-0.13788590216932878"), location.get().getLongitude());
        assertNotNull(location.get().getCreatedDate());
        assertEquals(task.getId(), location.get().getDeliveryTask().getId());
    }

    @ParameterizedTest
    @DisplayName("[Fail] Create location by courier by not courier.")
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_USER"})
    public void createDeliveryOrdersTest(String roleName) throws Exception {

        authentication(1L, roleName);
        createDeliveryTask();
        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_LOCATION_URL)
                        .content(readFileFromResources(BASE_PATH + "success-create-location.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create location by courier Empty longitude")
    public void createLocationEmptyLongitudeTest() throws Exception {

        authentication(1L, "ROLE_COURIER");
        var task = createDeliveryTask();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_LOCATION_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-location-empty-longitude.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create location by courier Empty latitude")
    public void createLocationEmptyLatitudeTest() throws Exception {

        authentication(1L, "ROLE_COURIER");
        createDeliveryTask();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_LOCATION_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-location-empty-latitude.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create location by courier Empty deliveryTaskId")
    public void createLocationEmptyTaskIdTest() throws Exception {

        authentication(1L, "ROLE_COURIER");
        createDeliveryTask();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_LOCATION_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-location-empty-task-id.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create location by courier Not found delivery task")
    @Transactional
    public void createLocationNotFoundTaskTest() throws Exception {

        authentication(10L, "ROLE_COURIER");
        createDeliveryTask();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_LOCATION_URL)
                        .content(readFileFromResources(BASE_PATH + "success-create-location.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail")
                        .value("Not found delivery task: [100] assigned with courier: [10]"))
                .andExpect(jsonPath("$.instance").value("/api/v1/locations"))
                .andDo(print());
    }
}
