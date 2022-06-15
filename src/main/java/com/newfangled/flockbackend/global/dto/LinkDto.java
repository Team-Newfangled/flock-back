package com.newfangled.flockbackend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class LinkDto {

    private String rel;
    private String method;
    private String href;

}
