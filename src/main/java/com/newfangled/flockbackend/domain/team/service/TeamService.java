package com.newfangled.flockbackend.domain.team.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    private TeamId getTeamId(Team team, Member member) {
        return new TeamId(team, member);
    }

    public TeamDto createTeam(Member member, NameDto nameDto) {
        Team team = teamRepository.save(new Team(null, nameDto.getName()));
        teamMemberRepository.save(new TeamMember(getTeamId(team, member), Role.Leader, true));
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
        TeamId teamId = getTeamId(findById(id), member);
        validatePermission(teamId);
        TeamMember target = teamMemberRepository.findByTeamId_Member_Id(userId)
                        .orElseThrow(TeamMember.NoMemberException::new);
        teamMemberRepository.delete(target);
    }

    private void validatePermission(TeamId teamId) {
        TeamMember leader = teamMemberRepository
                .findByTeamId(teamId)
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (leader.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

    }

    public PageDto<TeamMemberRO> findAllMember(Member member, long id, int page) {
        TeamId teamId = getTeamId(findById(id), member);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<TeamMemberRO> teamMembers = new PageImpl<>(
                teamMemberRepository.findAllByTeamIdAndApproved(teamId, true, pageable)
                        .stream().map(TeamMemberRO::new).collect(Collectors.toList())
        );
        return new PageDto<>(teamMembers);
    }

    public ProjectDto createProject(Member member, long id, NameDto nameDto) {
        Team team = findById(id);
        TeamMember leader = teamMemberRepository
                .findByTeamId(new TeamId(team, member))
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (leader.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        Project project = projectRepository
                .save(new Project(1L, team, nameDto.getName(), null));
        return new ProjectDto(project);
    }

    // 승인 대기 중인 회원들 조회
    public PageDto<TeamMemberRO> findNonApprovedMembers(Member member,
                                                        long id, int page) {
        TeamId teamId = new TeamId(findById(id), member);
        TeamMember leader = teamMemberRepository
                .findByTeamId(teamId)
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (leader.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<TeamMember> nonApprovedPage = teamMemberRepository
                .findAllByTeamIdAndApproved(teamId, false, pageable);
        return new PageDto<>(
                nonApprovedPage.getNumber(),
                nonApprovedPage.getTotalPages(),
                nonApprovedPage.stream().map(TeamMemberRO::new).collect(Collectors.toList())
        );
    }
    
    // 승인 대기 중인 회원 승인 하기
    public void approveTeamMember(Member member, long id, long teamMember) {
        Team team = findById(id);
        TeamId teamId = new TeamId(team, member);
        validatePermission(teamId);
        Member targetMember = memberRepository.findById(teamMember)
                .orElseThrow(Member.NotExistsException::new);
        TeamMember target = teamMemberRepository.findByTeamId(new TeamId(team, targetMember))
                .orElseThrow(TeamMember.NoMemberException::new);
        if (target.isApproved()) {
            throw new TeamMember.AlreadyApprovedException();
        }

        target.setApproved();
    }

    public boolean joinMember(long id, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(Member.NotExistsException::new);
        TeamId teamId = new TeamId(findById(id), member);
        if (teamMemberRepository.existsByTeamId(teamId)) {
            return false;
        }

        teamMemberRepository.save(new TeamMember(teamId, Role.Member, false));
        return true;
    }

    public TeamDto findTeamById(long teamId) {
        return new TeamDto(findById(teamId));
    }

    @Transactional(readOnly = true)
    protected Team findById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(Team.NotExistsException::new);
    }
}
