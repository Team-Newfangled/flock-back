package com.newfangled.flockbackend.domain.account.dto.response;

import com.newfangled.flockbackend.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileDto {

    private String nickname;
    private String image;
    private String company;

    public ProfileDto(Account account) {
        this.nickname = account.getOAuth().getName();
        this.company = account.getCompany();
        this.image = account.getOAuth().getPictureUrl();
    }
}
