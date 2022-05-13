package com.newfangled.flockbackend.global.config.redis;

import org.springframework.web.bind.annotation.ResponseStatus;

public class Redis {

    @ResponseStatus
    public static class InvalidKey extends RuntimeException {
        public InvalidKey(String key) {
            super(String.format("'%s' is invalid key", key));
        }
    }

}
