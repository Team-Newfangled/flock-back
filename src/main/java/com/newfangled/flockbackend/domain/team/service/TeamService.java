package com.newfangled.flockbackend.domain.team.service;

import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.repository.TeamRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private TeamId getTeamId(Team team) {
        return new TeamId(team);
    }

    public ProjectDto createTeam(Account account, NameDto nameDto) {
        Team team = teamRepository.save(new Team(null, nameDto.getName()));
        teamMemberRepository.save(new TeamMember(getTeamId(team), account, Role.Leader));
        return new ProjectDto(team.getId(), team.getName());
    }

    public PageDto<ProjectDto> findAllTeamProjects(long teamId, int page) {
        return null;
    }

    public void expulsionMember(long id, long userId) {

    }

    public PageDto<ProjectDto> findAllMember(long id, int page) {
        return null;
    }

    public ProjectDto createProject(NameDto nameDto) {
        return null;
    }
}
