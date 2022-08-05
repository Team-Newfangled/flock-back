package com.newfangled.flockbackend.domain.team.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamMemberRO;
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
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final ProjectRepository projectRepository;

    private TeamId getTeamId(Team team) {
        return new TeamId(team);
    }

    public TeamDto createTeam(Member member, NameDto nameDto) {
        Team team = teamRepository.save(new Team(null, nameDto.getName()));
        teamMemberRepository.save(new TeamMember(getTeamId(team), member, Role.Leader));
        return new TeamDto(team.getId(), team.getName());
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

    public void expulsionMember(Member member, long id, long userId) {
        Team team = findById(id);
        TeamMember leader = teamMemberRepository
                .findByMember_IdAndTeamId_Id(member.getId(), team.getId())
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (leader.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        TeamMember teamMember = teamMemberRepository
                .findByMember_IdAndTeamId_Id(userId, team.getId())
                .orElseThrow(TeamMember.NoMemberException::new);
        teamMemberRepository.delete(teamMember);
    }

    public PageDto<TeamMemberRO> findAllMember(long id, int page) {
        TeamId teamId = getTeamId(findById(id));
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<TeamMemberRO> teamMembers = new PageImpl<>(
                teamMemberRepository.findAllByTeamId(teamId, pageable)
                        .stream().map(TeamMemberRO::new).collect(Collectors.toList())
        );
        return new PageDto<>(teamMembers);
    }

    public ProjectDto createProject(long id, NameDto nameDto) {
        TeamId teamId = getTeamId(findById(id));
        Project project = projectRepository
                .save(new Project(null, teamId, nameDto.getName()));
        return new ProjectDto(project);
    }

    @Transactional(readOnly = true)
    protected Team findById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(Team.NotExistsException::new);
    }
}
