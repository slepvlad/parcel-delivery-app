package com.guavapay.parceldeliveryapp.client;

import com.guavapay.parceldeliveryapp.client.dto.AssignedOrderDto;
import com.guavapay.parceldeliveryapp.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(path = "/delivery-tasks/api/v1",
        contextId = "gateway-service",
        name = "gateway-service",
        configuration = OAuthFeignConfig.class)
public interface DeliveryTaskClient {

    @RequestMapping(method = RequestMethod.GET, path = "/delivery-tasks/courier")
    List<AssignedOrderDto> findAllAssigned();
}
