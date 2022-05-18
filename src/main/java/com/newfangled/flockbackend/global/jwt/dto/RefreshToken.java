package com.newfangled.flockbackend.global.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class RefreshToken implements Serializable {
    @JsonProperty(value = "account_id")
    private String accountId;
}
