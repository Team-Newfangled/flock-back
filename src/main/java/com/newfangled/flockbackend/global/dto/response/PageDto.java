package com.newfangled.flockbackend.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageDto<T> {

    private int page;

    @JsonProperty("page-count")
    private int pageCount;
    
    private List<T> results;

    public PageDto(Page<T> page) {
        this.page = page.getNumber();
        this.pageCount = page.getTotalPages();
        this.results = page.getContent();
    }
}
