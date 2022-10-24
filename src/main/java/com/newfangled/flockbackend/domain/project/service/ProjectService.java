package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.exception.BusinessException;
import com.newfangled.flockbackend.global.infra.UriValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UriValidator uriValidator;
    private final BoardService boardService;
    private final TodoService todoService;

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
        validateLeader(project, member);
        delete(project);
    }

    protected void delete(Project project) {
        boardService.deleteAllBoard(project);
        System.out.println("피드 삭제 명령");
        todoService.deleteAllTodoByProject(project);
        System.out.println("할 일 삭제 명령");
        projectRepository.delete(project);
    }

    public void deleteAllProjectsByTeam(Team team) {
        List<Project> projects = projectRepository.findAllByTeam(team);
        for (Project project : projects) {
            delete(project);
        }
    }

    public LinkListDto modifyProject(Member member, long projectId,
                                     NameDto nameDto) {
        Project project = findById(projectId);
        validateLeader(project, member);
        project.updateName(nameDto.getName());
        return new LinkListDto(
                "팀명을 변경하였습니다.",
                List.of(new LinkDto(
                        "self", "GET", String.format("/projects/%d", project.getId())
                ))
        );
    }

    public LinkListDto modifyProjectImg(Member member, long projectId,
                                        ContentDto contentDto) {
        String pictureURI = contentDto.getContent();
        if (!uriValidator.isUri(pictureURI)) {
            throw new BusinessException(HttpStatus.CONFLICT, "유효하지 않는 URI 입니다.");
        }

        Project project = findById(projectId);
        validateLeader(project, member);
        project.updateCoverImg(contentDto.getContent());
        return new LinkListDto(
                "프로젝트의 커버 사진을 변경하였습니다.",
                List.of(new LinkDto(
                        "self", "GET", String.format("/projects/%d", project.getId())
                ))
        );
    }

    @Transactional(readOnly = true)
    protected void validateLeader(Project project, Member member) {
        Team team = project.getTeam();
        TeamMember teamMember = teamMemberRepository
                .findByTeamAndMember(team, member)
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (teamMember.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }
    }
}
