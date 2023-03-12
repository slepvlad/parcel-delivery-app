package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.DeliveryOrderEvent;
import com.guavapay.parceldeliveryapp.dto.UpdateStatusRequest;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static com.guavapay.parceldeliveryapp.model.OrderStatus.CANCELED;
import static com.guavapay.parceldeliveryapp.model.OrderStatus.NEW;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChangeStatusDeliveryOrderTest extends AbstractControllerTest {

    private final static String CHANGE_STATUS_ORDER_URL = "/api/v1/delivery-orders/{orderId}/status";
    private static final String BASE_PATH = "requests/status/";

    @ParameterizedTest
    @DisplayName("[Success] Update delivery order status by admin")
    @EnumSource(value = OrderStatus.class, names = {"NEW", "ASSIGNED", "WAITING_FOR_DELIVERY", "ON_THE_WAY", "CANCELED", "DELIVERED"})
    public void updateDeliveryOrdersStatusTest(OrderStatus status) throws Exception {

        authentication(1L, "ROLE_ADMIN");
        var deliveryOrder = createDeliveryOrder(1L, status);
        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CHANGE_STATUS_ORDER_URL, deliveryOrder.getId())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());


        var order = deliveryOrderRepository.findById(deliveryOrder.getId());

        assertTrue(order.isPresent());
        assertEquals(deliveryOrder.getId(), order.get().getId());
        assertNotNull(order.get().getRequestId());
        assertEquals(1L, order.get().getUserId());
        assertEquals("Some test destination", order.get().getDestination());
        assertEquals(100L, order.get().getItemId());
        assertEquals(status, order.get().getOrderStatus());

    }

    @ParameterizedTest
    @DisplayName("[Fail] Update delivery order status by not admin.")
    @ValueSource(strings = {"ROLE_USER", "ROLE_COURIER"})
    public void updateDeliveryOrderStatusWrongRoleTest(String roleName) throws Exception {

        authentication(1L, roleName);
        var deliveryOrder = createDeliveryOrder(1L, NEW);

        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus(CANCELED);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CHANGE_STATUS_ORDER_URL, deliveryOrder.getId())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @DisplayName("[Fail] Update delivery order status by admin. Empty status")
    public void updateDeliveryOrdersEmptyDestinationTest() throws Exception {

        authentication(1L, "ROLE_ADMIN");
        var deliveryOrder = createDeliveryOrder(1L, NEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CHANGE_STATUS_ORDER_URL, deliveryOrder.getId())
                        .content(readFileFromResources(BASE_PATH + "fail-update-status-empty.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
