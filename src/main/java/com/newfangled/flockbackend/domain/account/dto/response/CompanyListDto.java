package com.newfangled.flockbackend.domain.account.dto.response;

import com.newfangled.flockbackend.domain.account.dto.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class CompanyListDto {

    private List<CompanyDto> result;

}
