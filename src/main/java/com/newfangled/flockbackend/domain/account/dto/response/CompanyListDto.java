package com.newfangled.flockbackend.domain.account.dto.response;

import com.newfangled.flockbackend.global.dto.NameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class CompanyListDto {

    private List<NameDto> result;

}
