package com.newfangled.flockbackend.domain.account.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TokenRefreshResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
