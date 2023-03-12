package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("[Success] create delivery order")
    @Disabled
    public void getAllOrdersTest() throws Exception {

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

}
