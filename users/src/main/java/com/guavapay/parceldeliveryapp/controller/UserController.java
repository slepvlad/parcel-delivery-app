package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.dto.UserDto;
import com.guavapay.parceldeliveryapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User controller", description = "user info controller")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/couriers")
    @Operation(
            summary = "[User stories: Admin] Can see list of couriers with their statuses",
            description = "List of couriers"
    )
    public List<UserDto> findAllCouriers(){
        return userService.findAllCouriers();
    }
}
