package com.newfangled.flockbackend.domain.account.controller;

import com.newfangled.flockbackend.domain.account.dto.request.TokenRefreshRequest;
import com.newfangled.flockbackend.domain.account.dto.response.OAuthResponseDto;
import com.newfangled.flockbackend.domain.account.dto.response.TokenRefreshResponse;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/oauth")
    public OAuthResponseDto oAuthGoogle(@RequestParam String code) {
        return authService.oAuthGoogle(code);
    }

    @GetMapping("/refresh")
    public TokenRefreshResponse refreshToken(
            @RequestBody @Valid TokenRefreshRequest tokenRefreshRequest,
            Authentication authentication
    ) {
        return authService.refreshToken(
                (Account) authentication.getPrincipal(), tokenRefreshRequest
        );
    }
}
