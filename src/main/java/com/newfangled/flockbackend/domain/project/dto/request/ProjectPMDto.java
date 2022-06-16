package com.newfangled.flockbackend.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class ProjectPMDto {

    @JsonProperty("pm-id")
    @NotNull
    private long projectManagerId;

}
