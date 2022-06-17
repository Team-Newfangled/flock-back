package com.newfangled.flockbackend.global.config.infra.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oauth2.google")
public class GoogleAuthConfiguration {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
