package com.newfangled.flockbackend.global.jwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponseDto {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
}
