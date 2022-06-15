package com.newfangled.flockbackend.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class ContentResponseDto {

    private long id;
    private String content;
    private long writer;

}
