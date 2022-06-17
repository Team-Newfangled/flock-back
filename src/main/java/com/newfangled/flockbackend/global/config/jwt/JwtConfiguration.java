package com.newfangled.flockbackend.global.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {
    private Long accessToken;
    private Long refreshToken;
    private String secretKey;
}
