package com.newfangled.flockbackend.service.project;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.domain.project.dto.request.TodoModifyDto;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.embed.TodoId;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoRepository;
import com.newfangled.flockbackend.domain.project.service.TodoService;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TodoServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Member member() {
        return Member.builder()
                .id(1L)
                .oAuth(null)
                .company("NewFangled")
                .role(UserRole.MEMBER)
                .build();
    }

    private void printTime(String title, long time) {
        System.out.printf("'%s 서비스 소요 시간': %d ms%n", title, time);
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

    private TodoDetail todoDetail(TeamMember teamMember, String content) {
        return new TodoDetail(1L, teamMember, content, getRandomHexColor(), null, null);
    }

    @DisplayName("할 일 작성 실패")
    @Test
    void writeTodoFailed() {
        StopWatch stopWatch = new StopWatch();
        // given
        Member member = member();
        Project project = new Project(1L, null, "Flock", null);

        lenient().when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));
        lenient().when(teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class)))
                .thenThrow(new TeamMember.NoMemberException());
        
        // when
        stopWatch.start();
        final TeamMember.NoPermissionException noPermissionException
                = assertThrows(
                        TeamMember.NoPermissionException.class,
                        () -> todoService.writeTodo(member, 1L, new ContentDto("할 일 서비스 구현"))
        );
        stopWatch.stop();

        // then
        assertThat(noPermissionException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        printTime("할 일 작성 실패", stopWatch.getTotalTimeMillis());
    }
    
    @DisplayName("할 일 작성 성공")
    @Test
    void writeTodoSuccess() {
        StopWatch stopWatch = new StopWatch();
        // given
        Member member = member();
        ContentDto contentDto = new ContentDto("할 일 서비스 구현");
        Project project = new Project(1L, null, "Flock", null);
        TeamMember teamMember = new TeamMember(null, member, Role.ForeignMember);
        Todo todo = new Todo(
                1L,
                new TodoId(project, todoDetail(teamMember, contentDto.getContent()))
        );

        lenient().when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));
        lenient().when(teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class)))
                .thenReturn(Optional.of(teamMember));
        lenient().when(todoRepository.save(any(Todo.class)))
                .thenReturn(todo);
    
        // when
        stopWatch.start();
        final TodoDto todoDto = todoService.writeTodo(member, 1L, contentDto);
        stopWatch.stop();
        
        // then
        assertThat(todoDto).isNotNull();
        assertThat(todoDto.getContent()).isEqualTo(contentDto.getContent());
        printTime("할 일 작성 성공", stopWatch.getTotalTimeMillis());
    }
    
    @DisplayName("할 일 수정 성공")
    @Test
    void modifyTodoSuccess() {
        StopWatch stopWatch = new StopWatch();
        // given
        Member member = member();
        Project project = new Project(1L, null, "Flock", null);
        TeamMember teamMember = new TeamMember(null, member, Role.Member);
        Todo todo = new Todo(
                1L, new TodoId(project, todoDetail(teamMember, "궔 서비스 추가"))
        );
        TodoModifyDto todoModifyDto
                = new TodoModifyDto("할 일 서비스 추가", null, null);

        lenient().when(todoRepository.findById(anyLong()))
                .thenReturn(Optional.of(todo));
        lenient().when(
                teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class))
        ).thenReturn(Optional.of(teamMember));

        // when
        stopWatch.start();
        final LinkListDto linkListDto
                = todoService.modifyTodo(member, 1L, todoModifyDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto).isNotNull();
        assertThat(todo.getTodoId().getTodoDetail().getContent())
                .isEqualTo(todoModifyDto.getContent());
        printTime("할 일 수정 성공", stopWatch.getTotalTimeMillis());
    }
}
