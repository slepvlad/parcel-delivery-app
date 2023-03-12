package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.DeliveryTaskEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SetStatusDeliveryTaskTest extends AbstractControllerTest {

    private final static String SET_DELIVERY_TASK_STATUS_URL = "/api/v1/delivery-tasks/{taskId}/status/{status}";

    @Test
    @DisplayName("[Success] Set delivery task status WAITING by courier ")
    public void setDeliveryTaskWaitingTest() throws Exception {

        authentication(1L, "ROLE_COURIER");

        var task = createDeliveryTask(ASSIGNED);
        ArgumentCaptor<DeliveryTaskEvent> captorDeliveryOrderEvent = ArgumentCaptor.forClass(DeliveryTaskEvent.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SET_DELIVERY_TASK_STATUS_URL, task.getId(), WAITING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();


        var deliveryTask = deliveryTaskRepository.findById(task.getId());

        assertTrue(deliveryTask.isPresent());
        assertEquals(task.getId(), deliveryTask.get().getId());
        assertEquals(1, deliveryTask.get().getCourierId());
        assertEquals(200, deliveryTask.get().getOrderId());
        assertNotNull(deliveryTask.get().getCreatedDate());
        assertNotNull(deliveryTask.get().getLastModifiedDate());
        assertEquals(WAITING, deliveryTask.get().getStatus());

        verify(rabbitTemplate).convertAndSend(acString.capture(), captorDeliveryOrderEvent.capture());
        List<String> stringValues = acString.getAllValues();
        assertEquals(1, stringValues.size());
        assertEquals("delivery-task-event", stringValues.get(0));

        List<DeliveryTaskEvent> events = captorDeliveryOrderEvent.getAllValues();
        assertEquals(1, events.size());
        assertEquals(200, events.get(0).getOrderId());
        assertEquals("WAITING", events.get(0).getStatus());
    }

    @Test
    @DisplayName("[Success] Set delivery task status ON_THE_WAY by courier ")
    public void setDeliveryTaskOnTheWayTest() throws Exception {

        authentication(1L, "ROLE_COURIER");

        var task = createDeliveryTask(WAITING);
        ArgumentCaptor<DeliveryTaskEvent> captorDeliveryOrderEvent = ArgumentCaptor.forClass(DeliveryTaskEvent.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SET_DELIVERY_TASK_STATUS_URL, task.getId(), ON_THE_WAY)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();


        var deliveryTask = deliveryTaskRepository.findById(task.getId());

        assertTrue(deliveryTask.isPresent());
        assertEquals(task.getId(), deliveryTask.get().getId());
        assertEquals(1, deliveryTask.get().getCourierId());
        assertEquals(200, deliveryTask.get().getOrderId());
        assertNotNull(deliveryTask.get().getCreatedDate());
        assertNotNull(deliveryTask.get().getLastModifiedDate());
        assertEquals(ON_THE_WAY, deliveryTask.get().getStatus());

        verify(rabbitTemplate).convertAndSend(acString.capture(), captorDeliveryOrderEvent.capture());
        List<String> stringValues = acString.getAllValues();
        assertEquals(1, stringValues.size());
        assertEquals("delivery-task-event", stringValues.get(0));

        List<DeliveryTaskEvent> events = captorDeliveryOrderEvent.getAllValues();
        assertEquals(1, events.size());
        assertEquals(200, events.get(0).getOrderId());
        assertEquals("ON_THE_WAY", events.get(0).getStatus());
    }

    @Test
    @DisplayName("[Success] Set delivery task status COMPLETED by courier")
    public void setDeliveryTaskCompletedTest() throws Exception {

        authentication(1L, "ROLE_COURIER");

        var task = createDeliveryTask(ON_THE_WAY);
        ArgumentCaptor<DeliveryTaskEvent> captorDeliveryOrderEvent = ArgumentCaptor.forClass(DeliveryTaskEvent.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SET_DELIVERY_TASK_STATUS_URL, task.getId(), COMPLETED)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();


        var deliveryTask = deliveryTaskRepository.findById(task.getId());

        assertTrue(deliveryTask.isPresent());
        assertEquals(task.getId(), deliveryTask.get().getId());
        assertEquals(1, deliveryTask.get().getCourierId());
        assertEquals(200, deliveryTask.get().getOrderId());
        assertNotNull(deliveryTask.get().getCreatedDate());
        assertNotNull(deliveryTask.get().getLastModifiedDate());
        assertEquals(COMPLETED, deliveryTask.get().getStatus());

        verify(rabbitTemplate).convertAndSend(acString.capture(), captorDeliveryOrderEvent.capture());
        List<String> stringValues = acString.getAllValues();
        assertEquals(1, stringValues.size());
        assertEquals("delivery-task-event", stringValues.get(0));

        List<DeliveryTaskEvent> events = captorDeliveryOrderEvent.getAllValues();
        assertEquals(1, events.size());
        assertEquals(200, events.get(0).getOrderId());
        assertEquals("COMPLETED", events.get(0).getStatus());
    }

    @ParameterizedTest
    @DisplayName("[Fail] Create delivery task by not admin.")
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_USER"})
    public void createDeliveryOrdersTest(String roleName) throws Exception {

        authentication(1L, roleName);

        var task = createDeliveryTask(ON_THE_WAY);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(SET_DELIVERY_TASK_STATUS_URL, task.getId(), COMPLETED)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}
