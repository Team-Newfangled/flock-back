package com.newfangled.flockbackend.domain.team.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final ProjectRepository projectRepository;

    private TeamId getTeamId(Team team) {
        return new TeamId(team);
    }

    public ProjectDto createTeam(Member member, NameDto nameDto) {
        Team team = teamRepository.save(new Team(null, nameDto.getName()));
        teamMemberRepository.save(new TeamMember(getTeamId(team), member, Role.Leader));
        return new ProjectDto(team.getId(), team.getName());
    }

    public PageDto<ProjectDto> findAllTeamProjects(long teamId, int page) {
        TeamId team = getTeamId(findById(teamId));
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<ProjectDto> teamProjects = new PageImpl<>(projectRepository.findAllByTeamId(team, pageable)
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList()));
        return new PageDto<>(teamProjects);
    }

    public void expulsionMember(long id, long userId) {

    }

    public PageDto<ProjectDto> findAllMember(long id, int page) {
        return null;
    }

    public ProjectDto createProject(NameDto nameDto) {
        return null;
    }

    @Transactional(readOnly = true)
    protected Team findById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(Team.NotExistsException::new);
    }
}
