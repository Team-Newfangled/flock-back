package com.newfangled.flockbackend.domain.member.dto.response;

import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class TeamListDto {

    private List<TeamDto> result;

}
