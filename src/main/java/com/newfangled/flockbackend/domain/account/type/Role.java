package com.newfangled.flockbackend.domain.account.type;

public enum Role {
    GUEST("ROLE_GUEST"),
    MEMBER("ROLE_USER");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
