package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.model.DeliveryOrder;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindAllByUserTest extends AbstractControllerTest {

    private final static String FIND_ALL_BY_USER_URL = "/api/v1/delivery-orders/user";

    @Test
    @DisplayName("[Success] get all orders by user")
    public void getAllOrdersTest() throws Exception {

        var requestId1 = UUID.randomUUID();
        var requestId2 = UUID.randomUUID();

        var order1 = new DeliveryOrder();
        order1.setOrderStatus(OrderStatus.DELIVERED);
        order1.setDestination("Destination 1");
        order1.setRequestId(requestId1);
        order1.setItemId(100L);
        order1.setUserId(1L);

        var order2 = new DeliveryOrder();
        order2.setOrderStatus(OrderStatus.NEW);
        order2.setDestination("Destination 2");
        order2.setRequestId(requestId2);
        order2.setItemId(100L);
        order2.setUserId(2L);

        deliveryOrderRepository.saveAll(List.of(order1, order2));

        authentication(1L, "ROLE_USER");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_BY_USER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].destination").value("Destination 1"))
                .andExpect(jsonPath("$[0].itemId").value(100))
                .andExpect(jsonPath("$[0].orderStatus").value("DELIVERED"))
                .andDo(print());
    }

    @DisplayName("[Fail] get all orders by user")
    @ParameterizedTest
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_COURIER"})
    public void failGetAllOrdersByAdminTest(String roleName) throws Exception {

        authentication(1L, roleName);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_BY_USER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @DisplayName("[Fail] Unauthorized get all orders by user")
    @Test
    public void failUnauthorizedGetAllOrdersByAdminTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_BY_USER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
