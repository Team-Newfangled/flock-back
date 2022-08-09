package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.request.TodoModifyDto;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.embed.TodoId;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoRepository;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TeamMemberRepository teamMemberRepository;
    private final ProjectRepository projectRepository;
    private final TodoRepository todoRepository;

    public TodoDto writeTodo(Member member, long projectId, ContentDto contentDto) {
        Project project = findProjectById(projectId);
        TeamMember teamMember = validateMember(project, member);
        Todo todo = new Todo(
                null,
                new TodoId(
                    project,
                    new TodoDetail(
                            null,
                            teamMember,
                            contentDto.getContent(),
                            getRandomHexColor(),
                            null,
                            null
                    )
                )
        );

        return new TodoDto(todoRepository.save(todo));
    }

    public LinkListDto modifyTodo(Member member, long todoId,
                                  TodoModifyDto todoModifyDto) {
        Todo todo = findById(todoId);
        Project project = todo.getTodoId().getProject();
        validateMember(project, member);
        TodoDetail todoDetail = todo.getTodoId().getTodoDetail();
        todoDetail.modifyDetail(
                todoModifyDto.getContent(),
                todoModifyDto.getStartDate(),
                todoModifyDto.getEndDate()
        );

        return new LinkListDto(
                "할 일을 수정했습니다.",
                List.of(new LinkDto("self", "GET", String.format("/todo/%d", todoId)))
        );
    }

    @Transactional(readOnly = true)
    protected Project findProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(Project.NotExistsException::new);
    }

    @Transactional(readOnly = true)
    protected TeamMember validateMember(Project project, Member member) {
        Team team = project.getTeam();
        return teamMemberRepository
                .findByMember_IdAndTeamId(member.getId(), new TeamId(team))
                .orElseThrow(TeamMember.NoPermissionException::new);
    }

    @Transactional(readOnly = true)
    protected Todo findById(long id) {
        return todoRepository.findById(id)
                .orElseThrow(Todo.NotExistsException::new);
    }

    private String getRandomHexColor() {
        byte[] bytes = new byte[3];
        new Random().nextBytes(bytes);
        StringBuilder result = new StringBuilder();
        for (byte temp : bytes) {
            result.append(String.format("%02x", temp));
        }

        return result.toString();
    }
}
