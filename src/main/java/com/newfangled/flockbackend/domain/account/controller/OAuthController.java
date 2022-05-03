package com.newfangled.flockbackend.domain.account.controller;

import com.newfangled.flockbackend.domain.account.dto.OAuthLoginResponse;
import com.newfangled.flockbackend.domain.account.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("redirect/oauth2/{provider}")
    public ResponseEntity<OAuthLoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        OAuthLoginResponse loginResponse = oAuthService.login(provider, code);
        return ResponseEntity.ok().body(loginResponse);
    }
}
