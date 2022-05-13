package com.newfangled.flockbackend.global.config.oauth;

import com.newfangled.flockbackend.domain.account.repository.InMemoryProviderRepository;
import com.newfangled.flockbackend.domain.account.type.OAuthProvider;
import com.newfangled.flockbackend.global.properties.OAuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@EnableConfigurationProperties(OAuthProperties.class)
@Configuration
public class OAuthConfig {

    private final OAuthProperties oAuthProperties;

    public OAuthConfig(OAuthProperties oAuthProperties) {
        this.oAuthProperties = oAuthProperties;
    }

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OAuthProvider> providers = OAuthAdapter.getOauthProviders(oAuthProperties);
        return new InMemoryProviderRepository(providers);
    }

}
