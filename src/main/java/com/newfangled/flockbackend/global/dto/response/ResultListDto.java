package com.newfangled.flockbackend.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResultListDto<T> {

    private List<T> results;

}
