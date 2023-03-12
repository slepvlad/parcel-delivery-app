package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.DeliveryTaskEvent;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus.ASSIGNED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateDeliveryTaskTest extends AbstractControllerTest {

    private final static String CREATE_DELIVERY_TASK_URL = "/api/v1/delivery-tasks";
    private static final String BASE_PATH = "requests/delivery_task/create/";

    @Test
    @DisplayName("[Success] Create delivery task by admin")
    public void createDeliveryTaskTest() throws Exception {

        authentication(1L, "ROLE_ADMIN");
        ArgumentCaptor<DeliveryTaskEvent> captorDeliveryOrderEvent = ArgumentCaptor.forClass(DeliveryTaskEvent.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_DELIVERY_TASK_URL)
                        .content(readFileFromResources(BASE_PATH + "success-create-delivery-task.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = mapper.readValue(response, LongIdWrapper.class).getId();

        var deliveryTask = deliveryTaskRepository.findById(id);

        assertTrue(deliveryTask.isPresent());
        assertEquals(id, deliveryTask.get().getId());
        assertEquals(200, deliveryTask.get().getCourierId());
        assertEquals(109, deliveryTask.get().getOrderId());
        assertNotNull(deliveryTask.get().getCreatedDate());
        assertNotNull(deliveryTask.get().getLastModifiedDate());
        assertEquals(ASSIGNED, deliveryTask.get().getStatus());

        verify(rabbitTemplate).convertAndSend(acString.capture(), captorDeliveryOrderEvent.capture());
        List<String> stringValues = acString.getAllValues();
        assertEquals(1, stringValues.size());
        assertEquals("delivery-task-event", stringValues.get(0));

        List<DeliveryTaskEvent> events = captorDeliveryOrderEvent.getAllValues();
        assertEquals(1, events.size());
        assertEquals(109, events.get(0).getOrderId());
        assertEquals("ASSIGNED", events.get(0).getStatus());
    }

    @ParameterizedTest
    @DisplayName("[Fail] Create delivery task by not admin.")
    @ValueSource(strings = {"ROLE_COURIER", "ROLE_USER"})
    public void createDeliveryOrdersTest(String roleName) throws Exception {

        authentication(1L, roleName);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_DELIVERY_TASK_URL)
                        .content(readFileFromResources(BASE_PATH + "success-create-delivery-task.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create delivery task by admin Empty order id")
    public void createDeliveryTaskEmptyOrderIdTest() throws Exception {

        authentication(1L, "ROLE_ADMIN");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_DELIVERY_TASK_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-delivery-task-empty-order-id.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("[Fail] Create delivery task by admin Empty courier id")
    public void createDeliveryTaskEmptyCourierIdTest() throws Exception {

        authentication(1L, "ROLE_ADMIN");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(CREATE_DELIVERY_TASK_URL)
                        .content(readFileFromResources(BASE_PATH + "fail-create-delivery-task-empty-courier-id.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}
