package com.newfangled.flockbackend.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class TokenRefreshRequest {

    @NotNull
    @JsonProperty("refresh_token")
    private String refreshToken;

}
