package com.newfangled.flockbackend.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class CompanyDto {

    @JsonProperty("company_name")
    private String companyName;

}
