package com.newfangled.flockbackend.domain.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class AccountProfileDto {

    private String oAuthId;
    private String email;
    private String imageUrl;
    private String name;
}
