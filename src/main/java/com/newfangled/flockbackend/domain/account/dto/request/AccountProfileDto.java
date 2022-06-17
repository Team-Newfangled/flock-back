package com.newfangled.flockbackend.domain.account.dto.request;

import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.type.UserRole;
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
