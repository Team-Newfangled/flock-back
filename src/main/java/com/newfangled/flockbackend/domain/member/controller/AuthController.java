package com.newfangled.flockbackend.domain.member.controller;

import com.newfangled.flockbackend.domain.member.dto.request.TokenRefreshRequest;
import com.newfangled.flockbackend.domain.member.dto.response.OAuthResponseDto;
import com.newfangled.flockbackend.domain.member.dto.response.TokenRefreshResponse;
import com.newfangled.flockbackend.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/oauth")
    public OAuthResponseDto oAuthGoogle(@RequestParam String code) {
        System.out.println(authService);
        System.out.println(code);
        return authService.oAuthGoogle(code);
    }

    @PostMapping("/refresh")
    public TokenRefreshResponse refreshToken(
            @RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest
    ) {
        return authService.refreshToken(tokenRefreshRequest);
    }
}
