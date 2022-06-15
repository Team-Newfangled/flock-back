package com.newfangled.flockbackend.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class ProjectPMDto {

    @JsonProperty("pm-id")
    private long projectManagerId;

}
