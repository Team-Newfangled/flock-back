package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.request.TodoCompleteDto;
import com.newfangled.flockbackend.domain.project.dto.request.TodoModifyDto;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoDetailRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoRepository;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TeamMemberRepository teamMemberRepository;
    private final ProjectRepository projectRepository;
    private final TodoRepository todoRepository;
    private final TodoDetailRepository todoDetailRepository;

    public TodoDto writeTodo(Member member, long projectId, ContentDto contentDto) {
        Project project = findProjectById(projectId);
        TeamMember teamMember = validateMember(project, member);
        if (teamMember.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        Todo todo = Todo.builder()
                .project(project)
                .completed(false)
                .build();
        Todo savedTodo = todoRepository.save(todo);
        LocalDate date = LocalDate.now();

        TodoDetail todoDetail = TodoDetail.builder()
                .teamMember(teamMember)
                .content(contentDto.getContent())
                .color(getRandomHexColor())
                .startDate(date)
                .endDate(date.plusDays(3))
                .build();
        TodoDetail savedDetail = todoDetailRepository.save(todoDetail);
        todo.setTodoDetail(savedDetail);
        savedDetail.setTodo(todo);
        return new TodoDto(savedTodo);
    }

    public LinkListDto modifyTodo(Member member, long todoId,
                                  TodoModifyDto todoModifyDto) {
        Todo todo = findById(todoId);
        Project project = todo.getProject();
        TeamMember teamMember = validateMember(project, member);
        TodoDetail todoDetail = todo.getTodoDetail();
        final long manager = todoDetail.getTeamMember().getId();
        if (teamMember.getId() != manager && teamMember.getRole() != Role.Leader) {
            // 담당자도 아니고, 팀장도 아니라면, 권한 없음
            throw new TeamMember.NoPermissionException();
        }

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
    
    public void deleteTodo(Member member, long todoId) {
        Todo todo = findById(todoId);
        Project project = todo.getProject();
        TeamMember teamMember = validateMember(project, member);
        if (teamMember.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        todoRepository.delete(todo);
    }
    
    public void deleteAllTodoByProject(Project project) {
        todoRepository.deleteAllByProject(project);
    }

    public LinkListDto completeTodo(Member member, long todoId,
                                    final TodoCompleteDto todoCompleteDto) {
        Todo todo = findById(todoId);
        Project project = todo.getProject();
        validateMember(project, member);
        todo.setCompleted(todoCompleteDto.isComplete());
        todoRepository.save(todo);
        return new LinkListDto(
                "할 일을 완료했습니다.",
                List.of(new LinkDto("self", "GET", String.format("/todo/%d", todoId)))
        );
    }

    public PageDto<TodoDto> findAllTodosByMember(Member member, long projectId,
                                                 long userId, int page) {
        Project project = findProjectById(projectId);
        validateMember(project, member);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Todo> todos = todoRepository
                .findAllByProjectAndMemberId(project, userId, pageable);
        return new PageDto<>(
                todos.getNumber(),
                todos.getTotalPages(),
                todos.stream().map(TodoDto::new).collect(Collectors.toList())
        );
    }

    public PageDto<TodoDto> findAll(Member member, long projectId, int page) {
        Project project = findProjectById(projectId);
        validateMember(project, member);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Todo> todos = todoRepository.findAllByProject(project, pageable);
        return new PageDto<>(
                todos.getNumber(),
                todos.getTotalPages(),
                todos.stream().map(TodoDto::new).collect(Collectors.toList())
        );
    }

    public TodoDto findTodo(long todoId) {
        return new TodoDto(findById(todoId));
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
                .findByTeamAndMember(team, member)
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
