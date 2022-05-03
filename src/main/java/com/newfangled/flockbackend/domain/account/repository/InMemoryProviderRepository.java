package com.newfangled.flockbackend.domain.account.repository;

import com.newfangled.flockbackend.domain.account.type.OAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProviderRepository {

    private final Map<String, OAuthProvider> providers;

    public InMemoryProviderRepository(Map<String, OAuthProvider> providers) {
        this.providers = new HashMap<>(providers);
    }

    public OAuthProvider findByProviderName(String name) {
        return providers.get(name);
    }
}
