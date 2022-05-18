package com.newfangled.flockbackend.domain.account.dto;

import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.type.Role;
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

    public Account toAccount() {
        OAuth oAuth = OAuth.builder()
                .email(email)
                .oauthId(oAuthId)
                .profileImage(imageUrl)
                .name(name)
                .build();
        return Account.builder()
                .oAuth(oAuth)
                .role(Role.GUEST)
                .build();
    }
}
