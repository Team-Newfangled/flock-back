package com.newfangled.flockbackend.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class ListDto<T> {

    private int page;

    @JsonProperty("page-count")
    private int pageCount;
    
    private List<T> results;

}
