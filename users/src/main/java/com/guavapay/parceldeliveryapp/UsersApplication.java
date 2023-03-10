package com.guavapay.parceldeliveryapp;

import com.guavapay.parceldeliveryapp.config.RsaKeyProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableDiscoveryClient
@OpenAPIDefinition(
		info = @Info(title = "Parcel Delivery App",
				description = "Authentication service"))
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

}
