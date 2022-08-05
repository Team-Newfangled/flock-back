package com.newfangled.flockbackend.global.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionDto {

    @JsonProperty(value = "timestamp", index = 0)
    private final LocalDateTime timeStamp = LocalDateTime.now();

    private final String message;
}
