package com.newfangled.flockbackend.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class AccountResponseDto {

    private String nickname;
    private String email;
    private String image;

}
