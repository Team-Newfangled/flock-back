package com.newfangled.flockbackend.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class ProjectDto {

    private long id;
    private String name;

    @JsonProperty("self-url")
    private String selfUrl;

}
