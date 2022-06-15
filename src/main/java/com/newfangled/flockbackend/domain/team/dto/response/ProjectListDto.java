package com.newfangled.flockbackend.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class ProjectListDto {

    private int page;

    @JsonProperty("page_count")
    private int pageCount;

    private List<ProjectDto> results;

}
