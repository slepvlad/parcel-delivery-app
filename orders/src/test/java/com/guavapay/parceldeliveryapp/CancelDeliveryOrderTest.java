package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.DeliveryOrderEvent;
import com.guavapay.parceldeliveryapp.model.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.guavapay.parceldeliveryapp.model.OrderStatus.NEW;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CancelDeliveryOrderTest extends AbstractControllerTest {

    private final static String CANCEL_ORDER_URL = "/api/v1/delivery-orders/{orderId}/cancel";

    @ParameterizedTest
    @DisplayName("[Success] Cancel delivery order by user")
    @EnumSource(value = OrderStatus.class, names = {"NEW", "ASSIGNED", "WAITING_FOR_DELIVERY"})
    public void createDeliveryOrdersTest(OrderStatus status) throws Exception {

        authentication(1L, "ROLE_USER");
        var deliveryOrder = createDeliveryOrder(1L, status);
        ArgumentCaptor<DeliveryOrderEvent> captorDeliveryOrderEvent = ArgumentCaptor.forClass(DeliveryOrderEvent.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CANCEL_ORDER_URL, deliveryOrder.getId())
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
        assertEquals(OrderStatus.CANCELED, order.get().getOrderStatus());

        verify(rabbitTemplate).convertAndSend(acString.capture(), captorDeliveryOrderEvent.capture());
        List<String> stringValues = acString.getAllValues();
        assertEquals(1, stringValues.size());
        assertEquals("delivery-order-event", stringValues.get(0));

        List<DeliveryOrderEvent> events = captorDeliveryOrderEvent.getAllValues();
        assertEquals(1, events.size());
        assertEquals(deliveryOrder.getId(), events.get(0).getOrderId());
        assertEquals("CANCELED", events.get(0).getStatus());
    }


    @ParameterizedTest
    @DisplayName("[Fail] Cancel delivery order by user Current status can't be cancelled")
    @EnumSource(value = OrderStatus.class, names = {"ON_THE_WAY", "CANCELED", "DELIVERED"})
    public void createDeliveryOrdersWrongStatusTest(OrderStatus status) throws Exception {

        authentication(1L, "ROLE_USER");
        var deliveryOrder = createDeliveryOrder(1L, status);
        ArgumentCaptor<DeliveryOrderEvent> captorDeliveryOrderEvent = ArgumentCaptor.forClass(DeliveryOrderEvent.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CANCEL_ORDER_URL, deliveryOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.detail")
                        .value(format("Order with status: [%s] can't be cancelled", status.name())))
                .andExpect(jsonPath("$.instance")
                        .value(format("/api/v1/delivery-orders/%d/cancel", deliveryOrder.getId())))
                .andDo(print());

        var order = deliveryOrderRepository.findById(deliveryOrder.getId());

        assertTrue(order.isPresent());
        assertEquals(deliveryOrder.getId(), order.get().getId());
        assertNotNull(order.get().getRequestId());
        assertEquals(1L, order.get().getUserId());
        assertEquals("Some test destination", order.get().getDestination());
        assertEquals(100L, order.get().getItemId());
        assertEquals(status, order.get().getOrderStatus());

        verify(rabbitTemplate, times(0)).convertAndSend(acString.capture(), captorDeliveryOrderEvent.capture());
    }

    @ParameterizedTest
    @DisplayName("[Fail] Cancel delivery order by not user.")
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_COURIER"})
    public void createDeliveryOrdersWrongRoleTest(String roleName) throws Exception {

        authentication(1L, roleName);
        var deliveryOrder = createDeliveryOrder(1L, NEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CANCEL_ORDER_URL, deliveryOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Cancel delivery order by not user.")
    public void createDeliveryOrdersWrongUserTest() throws Exception {

        authentication(1L, "ROLE_USER");
        var deliveryOrder = createDeliveryOrder(2L, NEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CANCEL_ORDER_URL, deliveryOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail")
                        .value(format("Not found order with orderId:[%d]", deliveryOrder.getId())))
                .andExpect(jsonPath("$.instance")
                        .value(format("/api/v1/delivery-orders/%d/cancel", deliveryOrder.getId())))
                .andDo(print());
    }
}
