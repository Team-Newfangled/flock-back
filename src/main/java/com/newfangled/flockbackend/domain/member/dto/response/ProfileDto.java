package com.newfangled.flockbackend.domain.member.dto.response;

import com.newfangled.flockbackend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileDto {

    private String nickname;
    private String image;
    private String company;

    public ProfileDto(Member account) {
        this.nickname = account.getOAuth().getName();
        this.company = account.getCompany();
        this.image = account.getOAuth().getPictureUrl();
    }
}
