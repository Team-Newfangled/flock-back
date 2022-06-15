package com.newfangled.flockbackend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class LinkListDto {

    private String message;
    private List<LinkDto> links;

}
