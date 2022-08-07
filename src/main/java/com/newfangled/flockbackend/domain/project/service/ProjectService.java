package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional(readOnly = true)
    protected Project findById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(Project.NotExistsException::new);
    }

    public ProjectDto findProject(long projectId) {
        return new ProjectDto(findById(projectId));
    }

    public void deleteProject(Member member, long projectId) {
        Project project = findById(projectId);
        Team team = project.getTeam();
        TeamMember teamMember = teamMemberRepository
                .findByMember_IdAndTeamId(member.getId(), new TeamId(team))
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (teamMember.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        projectRepository.delete(findById(projectId));
    }

//    public LinkListDto modifyProject(Member member, long projectId,
//                                     NameDto nameDto) {
//        // projectId 와 member 를 사용하여 정확하게 팀을 가져올 수 있는 가?
//
//
//    }

}
