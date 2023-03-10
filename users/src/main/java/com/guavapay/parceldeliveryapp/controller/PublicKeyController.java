package com.guavapay.parceldeliveryapp.controller;

import com.guavapay.parceldeliveryapp.config.RsaKeyProperties;
import com.guavapay.parceldeliveryapp.dto.StringWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("api/v1/public-key")
@RequiredArgsConstructor
public class PublicKeyController {

    private final RsaKeyProperties jwtConfigProperties;

    @GetMapping
    public StringWrapper getPublicKey() {
        var publicKeyByte = jwtConfigProperties.publicKey().getEncoded();
        var str = Base64.getEncoder().encodeToString(publicKeyByte);
        return new StringWrapper(str);
    }
}
