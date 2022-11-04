package com.newfangled.flockbackend.domain.team.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.service.ProjectService;
import com.newfangled.flockbackend.domain.team.dto.response.MemberRoleRO;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import com.newfangled.flockbackend.domain.member.dto.response.TeamMemberRO;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.repository.TeamRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
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
    private final MemberRepository memberRepository;
    private final ProjectService projectService;

    public TeamDto createTeam(Member member, NameDto nameDto) {
        Team team = teamRepository.save(
                Team.builder()
                        .name(nameDto.getName())
                        .build()
        );
        teamMemberRepository.save(
                TeamMember.builder()
                        .team(team)
                        .member(member)
                        .role(Role.Leader)
                        .approved(true)
                        .build()
        );
        return new TeamDto(team.getId(), team.getName());
    }

    public PageDto<ProjectDto> findAllTeamProjects(long teamId, int page) {
        Team team = findById(teamId);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<ProjectDto> teamProjects = new PageImpl<>(projectRepository.findAllByTeam(team, pageable)
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList()));
        return new PageDto<>(teamProjects);
    }

    // 승인 및 미승인 팀원까지 사용할 수 있음
    // 추후 로직이 나뉠 수도 있음
    public void expulsionMember(Member member, long id, long userId) {
        Team team = findById(id);
        validatePermission(team, member);
        TeamMember target = teamMemberRepository
                .findByTeamId_Member_IdAndTeamId_Team(userId, team)
                .orElseThrow(TeamMember.NoMemberException::new);
        teamMemberRepository.delete(target);
    }

    private void validatePermission(Team team, Member member) {
        TeamMember leader = teamMemberRepository
                .findByTeamAndMember(team, member)
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (leader.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }
    }

    public PageDto<TeamMemberRO> findAllMember(Member member, long id, int page) {
        Team team = findById(id);
        Pageable pageable = PageRequest.of(page, 10);
        Page<TeamMemberRO> teamMembers = new PageImpl<>(
                teamMemberRepository.findAllByTeamAndApproved(team, true, pageable)
                        .stream().map(TeamMemberRO::new).collect(Collectors.toList())
        );
        return new PageDto<>(teamMembers);
    }

    public ProjectDto createProject(Member member, long id, NameDto nameDto) {
        Team team = findById(id);
        TeamMember leader = teamMemberRepository
                .findByTeamAndMember(team, member)
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (leader.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        Project project = projectRepository
                .save(new Project(null, team, nameDto.getName(), null));
        return new ProjectDto(project);
    }

    // 승인 대기 중인 회원들 조회
    public PageDto<TeamMemberRO> findNonApprovedMembers(Member member,
                                                        long id, int page) {
        Team team = findById(id);
        TeamMember leader = teamMemberRepository
                .findByTeamAndMember(team, member)
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (leader.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<TeamMember> nonApprovedPage = teamMemberRepository
                .findAllByTeamAndApproved(team, false, pageable);
        return new PageDto<>(
                nonApprovedPage.getNumber(),
                nonApprovedPage.getTotalPages(),
                nonApprovedPage.stream().map(TeamMemberRO::new).collect(Collectors.toList())
        );
    }
    
    // 승인 대기 중인 회원 승인 하기
    public void approveTeamMember(Member member, long id, long teamMember) {
        Team team = findById(id);
        validatePermission(team, member);
        Member targetMember = memberRepository.findById(teamMember)
                .orElseThrow(Member.NotExistsException::new);
        TeamMember target = teamMemberRepository.findByTeamAndMember(team, targetMember)
                .orElseThrow(TeamMember.NoMemberException::new);
        if (target.isApproved()) {
            throw new TeamMember.AlreadyApprovedException();
        }

        target.setApproved();
    }

    public boolean joinMember(long id, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(Member.NotExistsException::new);
        Team team = findById(id);
        if (teamMemberRepository.existsByTeamAndMember(team, member)) {
            return false;
        }

        teamMemberRepository.save(
                TeamMember.builder()
                        .team(team)
                        .member(member)
                        .role(Role.Member)
                        .approved(false)
                        .build()
        );
        return true;
    }

    public TeamDto findTeamById(long teamId) {
        return new TeamDto(findById(teamId));
    }

    public MemberRoleRO findRoleById(Member member, long teamId) {
        Team team = findById(teamId);
        TeamMember teamMember = teamMemberRepository.findByTeamAndMember(team, member)
                .orElseThrow(TeamMember.NoMemberException::new);
        return new MemberRoleRO(teamMember.getRole().name());
    }
    
    public void deleteTeam(Member member, long id) {
        Team team = findById(id);
        TeamMember teamMember = teamMemberRepository.findByTeamAndMember(team, member)
                .orElseThrow(TeamMember.NoMemberException::new);
        if (teamMember.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        projectService.deleteAllProjectsByTeam(team);
//        teamMemberRepository.deleteAllByTeamId_Team(team);
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    protected Team findById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(Team.NotExistsException::new);
    }
}
