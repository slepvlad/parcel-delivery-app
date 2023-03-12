package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateDeliveryOrderTest extends AbstractControllerTest {

    private final static String CREATE_ORDER_URL = "/api/v1/delivery-orders";
    private static final String BASE_PATH = "requests/create/";

    @Test
    @DisplayName("[Success] create delivery order by user")
    public void createDeliveryOrdersTest() throws Exception {

        authentication(1L, "ROLE_USER");

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_ORDER_URL)
                        .content(readFileFromResources(BASE_PATH + "success-create-delivery-order.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        Long id = objectMapper.readValue(response, LongIdWrapper.class).getId();

        var order = deliveryOrderRepository.findById(id);

        assertTrue(order.isPresent());
        assertEquals(id, order.get().getId());
        assertEquals(id, order.get().getId());
        assertEquals(UUID.fromString("d28e8e89-2eaa-4ab7-aa5f-24e6df9d15cb"), order.get().getRequestId());
        assertEquals(1L, order.get().getUserId());
        assertEquals("Some destination", order.get().getDestination());
        assertEquals(100L, order.get().getItemId());
        assertEquals(OrderStatus.NEW, order.get().getOrderStatus());
    }

    @Test
    @DisplayName("[Fail] Create delivery order by user. RequestId is null")
    public void createDeliveryOrdersNullRequestIdTest() throws Exception {

        authentication(1L, "ROLE_USER");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_ORDER_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-delivery-order-null-request-id.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create delivery order by user. Empty destination")
    public void createDeliveryOrdersEmptyDestinationTest() throws Exception {

        authentication(1L, "ROLE_USER");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_ORDER_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-delivery-order-null-destination.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create delivery order by user. OrderId is null")
    public void createDeliveryOrdersNullOrderIdTest() throws Exception {

        authentication(1L, "ROLE_USER");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_ORDER_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-delivery-order-null-order-id.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @DisplayName("[Fail] Create delivery order by not user.")
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_COURIER"})
    public void createDeliveryOrdersTest(String roleName) throws Exception {

        authentication(1L, roleName);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_ORDER_URL)
                        .content(readFileFromResources(BASE_PATH + "success-create-delivery-order.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

}
