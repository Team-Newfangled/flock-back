package com.newfangled.flockbackend.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamDto {

    @JsonProperty("team-id")
    private long teamId;

    @JsonProperty("team-name")
    private String teamName;

}
