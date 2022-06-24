package com.newfangled.flockbackend.domain.account.dto.response;

import com.newfangled.flockbackend.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AccountResponseDto {

    private String nickname;
    private String image;

    public AccountResponseDto(Account account) {
        this.nickname = account.getOAuth().getName();
        this.image = account.getOAuth().getPictureUrl();
    }
}
