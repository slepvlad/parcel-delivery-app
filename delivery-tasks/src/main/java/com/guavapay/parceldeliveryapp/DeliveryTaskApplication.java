package com.guavapay.parceldeliveryapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8888/delivery-tasks/")
        },
        info = @Info(title = "Parcel Delivery App",
                description = "Parcel delivery task service"))
public class DeliveryTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryTaskApplication.class, args);
    }
}
