package com.newfangled.flockbackend.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamDto {

    @JsonProperty("team-id")
    private long teamId;

    @JsonProperty("team-name")
    private String teamName;

    public TeamDto(Team team) {
        this.teamId = team.getId();
        this.teamName = team.getName();
    }

}
