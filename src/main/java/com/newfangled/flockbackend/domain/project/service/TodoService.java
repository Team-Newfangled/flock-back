package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.request.TodoCompleteDto;
import com.newfangled.flockbackend.domain.project.dto.request.TodoModifyDto;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.embed.DetailId;
import com.newfangled.flockbackend.domain.project.embed.TodoId;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoDetailRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoRepository;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import com.newfangled.flockbackend.global.embed.TeamId;
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

        Todo todo = new Todo(
                null,
                null,
                false
        );
        Todo savedTodo = todoRepository.save(todo);
        LocalDate date = LocalDate.now();

        TodoDetail todoDetail = new TodoDetail(
                new DetailId(todo),
                teamMember,
                contentDto.getContent(),
                getRandomHexColor(),
                date,
                date.plusDays(3)
        );
        TodoDetail savedDetail = todoDetailRepository.save(todoDetail);
        todo.setTodoId(new TodoId(project, savedDetail));

        return new TodoDto(savedTodo);
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
    
    public void deleteTodo(Member member, long todoId) {
        Todo todo = findById(todoId);
        Project project = todo.getTodoId().getProject();
        validateMember(project, member);
        todoDetailRepository.deleteById(new DetailId(todo));
        todoRepository.delete(todo);
    }

    public LinkListDto completeTodo(Member member, long todoId,
                                    final TodoCompleteDto todoCompleteDto) {
        Todo todo = findById(todoId);
        Project project = todo.getTodoId().getProject();
        validateMember(project, member);
        todo.setCompleted(todoCompleteDto.isComplete());
        todoRepository.save(todo);
        return new LinkListDto(
                "할 일을 완료했습니다.",
                List.of(new LinkDto("self", "GET", String.format("/todo/%d", todoId)))
        );
    }

    public PageDto<TodoDto> findAllTodos(Member member, long projectId,
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
                .findByTeamId(new TeamId(team, member))
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
