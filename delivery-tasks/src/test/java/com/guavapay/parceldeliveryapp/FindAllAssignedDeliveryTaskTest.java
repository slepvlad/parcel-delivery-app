package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.dto.DeliveryTaskEvent;
import com.guavapay.parceldeliveryapp.dto.LongIdWrapper;
import com.guavapay.parceldeliveryapp.model.DeliveryTask;
import com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.guavapay.parceldeliveryapp.model.DeliveryTaskStatus.ASSIGNED;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindAllAssignedDeliveryTaskTest extends AbstractControllerTest {

    private final static String FIND_ALL_ASSIGNED_URL = "/api/v1/delivery-tasks/courier";

    @Test
    @DisplayName("[Success] Find all assigned delivery task by courier")
    public void createDeliveryTaskTest() throws Exception {

        authentication(1L, "ROLE_COURIER");

        var deliveryTask1 = new DeliveryTask();
        deliveryTask1.setCourierId(1L);
        deliveryTask1.setStatus(DeliveryTaskStatus.ASSIGNED);
        deliveryTask1.setOrderId(200L);

        var deliveryTask2 = new DeliveryTask();
        deliveryTask2.setCourierId(1L);
        deliveryTask2.setStatus(DeliveryTaskStatus.ON_THE_WAY);
        deliveryTask2.setOrderId(150L);

        var deliveryTask3 = new DeliveryTask();
        deliveryTask3.setCourierId(20L);
        deliveryTask3.setStatus(DeliveryTaskStatus.ON_THE_WAY);
        deliveryTask3.setOrderId(160L);

        deliveryTaskRepository.saveAll(List.of(deliveryTask1, deliveryTask2, deliveryTask3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_ASSIGNED_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].courierId").value(1))
                .andExpect(jsonPath("$[0].orderId").value(200))
                .andExpect(jsonPath("$[0].status").value("ASSIGNED"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].courierId").value(1))
                .andExpect(jsonPath("$[1].orderId").value(150))
                .andExpect(jsonPath("$[1].status").value("ON_THE_WAY"))
                .andDo(print());
    }

    @ParameterizedTest
    @DisplayName("[Fail] Find all assigned delivery task not by courier")
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_USER"})
    public void createDeliveryOrdersTest(String roleName) throws Exception {

        authentication(1L, roleName);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(FIND_ALL_ASSIGNED_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

}
