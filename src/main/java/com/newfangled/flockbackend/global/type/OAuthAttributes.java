package com.newfangled.flockbackend.global.type;

import com.newfangled.flockbackend.domain.account.dto.AccountProfileDto;

import java.util.Arrays;
import java.util.Map;

public enum OAuthAttributes {
    GOOGLE("google") {
        @Override
        public AccountProfileDto of(Map<String, Object> attributes) {
            return AccountProfileDto.builder()
                    .oAuthId(String.valueOf(attributes.get("sub")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("picture"))
                    .build();
        }
    };

    private final String providerName;

    OAuthAttributes(String name) {
        this.providerName = name;
    }

    public static AccountProfileDto extract(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }

    public abstract AccountProfileDto of(Map<String, Object> attributes);

}
