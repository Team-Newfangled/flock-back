package com.newfangled.flockbackend.service.project;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.service.ProjectService;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import com.newfangled.flockbackend.global.infra.UriValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Spy
    private UriValidator uriValidator;

    @InjectMocks
    private ProjectService projectService;

    private void printTime(String title, long time) {
        System.out.printf("'%s 서비스 소요 시간': %d ms%n", title, time);
    }

    @DisplayName("프로젝트 조회 살패 테스트")
    @Test
    void findProjectFailed() {
        StopWatch stopWatch = new StopWatch();
        // given
        lenient().when(projectRepository.findById(anyLong()))
                .thenThrow(new Project.NotExistsException());
    
        // when
        stopWatch.start();
        Project.NotExistsException notExistsException
                = assertThrows(
                        Project.NotExistsException.class,
                        () -> projectService.findProject(anyLong())
        );
        stopWatch.stop();
        
        // then
        assertThat(notExistsException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        printTime("프로젝트 조회 실패 테스트", stopWatch.getTotalTimeMillis());
    }

    @DisplayName("프로젝트 조회 성공 테스트")
    @Test
    void findProjectSuccess() {
        StopWatch stopWatch = new StopWatch();
        // given
        Project project = new Project(1L, null, "Flock", null);

        lenient().when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));

        // when
        stopWatch.start();
        ProjectDto projectDto = projectService.findProject(1L);
        stopWatch.stop();

        // then
        assertThat(projectDto).isNotNull();
        assertThat(projectDto.getName()).isEqualTo(project.getName());
        printTime("프로젝트 조회 성공 테스트", stopWatch.getTotalTimeMillis());
    }

    @DisplayName("프로젝트 삭제 실패 테스트")
    @Test
    void deleteProjectFailed() {
        StopWatch stopWatch = new StopWatch();
        // given
        lenient().when(projectRepository.findById(anyLong()))
                .thenThrow(new Project.NotExistsException());
    
        // when
        stopWatch.start();
        Project.NotExistsException notExistsException
                = assertThrows(
                Project.NotExistsException.class,
                () -> projectService.deleteProject(new Member(), 1L)
        );
        stopWatch.stop();

        // then
        assertThat(notExistsException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        printTime("프로젝트 삭제 실패 테스트", stopWatch.getTotalTimeMillis());
    }
    
    @DisplayName("프로젝트 삭제 성공 테스트")
    @Test
    void deleteProjectSuccess() {
        StopWatch stopWatch = new StopWatch();
        // given
        Member member = new Member(1L, null, UserRole.MEMBER, "DGSW");

        doNothing().when(projectRepository).delete(any(Project.class));
        lenient().when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Project(1L, new Team(), "Flock", null)));
        lenient().when(teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class)))
                .thenReturn(Optional.of(new TeamMember(new TeamId(), member, Role.Leader, true)));

        // when
        stopWatch.start();
        projectService.deleteProject(member, 1L);
        stopWatch.stop();
        
        // then
        printTime("프로젝트 삭제 성공 테스트", stopWatch.getTotalTimeMillis());

        verify(projectRepository, times(1)).delete(any(Project.class));
    }

    @DisplayName("프로젝트 수정 실패 테스트")
    @Test
    void modifyProjectFailed() {
        StopWatch stopWatch = new StopWatch();
        // given
        lenient().when(projectRepository.findById(anyLong()))
                .thenThrow(new Project.NotExistsException());

        // when
        stopWatch.start();
        Project.NotExistsException notExistsException
                = assertThrows(
                Project.NotExistsException.class,
                () -> projectService.modifyProject(new Member(), 1L, new NameDto())
        );
        stopWatch.stop();

        // then
        assertThat(notExistsException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        printTime("프로젝트 수정 실패 테스트", stopWatch.getTotalTimeMillis());
    }

    @DisplayName("프로젝트 수정 성공 테스트")
    @Test
    void modifyProjectSuccess() {
        StopWatch stopWatch = new StopWatch();
        // given
        Member member = new Member(1L, null, UserRole.MEMBER, "DGSW");
        NameDto nameDto = new NameDto("끼리끼리");
        Project project = new Project(1L, null, "Flock", null);
        TeamMember teamMember = new TeamMember(null, member, Role.Leader, true);

        lenient().when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));
        lenient().when(teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class)))
                .thenReturn(Optional.of(teamMember));

        // when
        stopWatch.start();
        LinkListDto linkListDto = projectService.modifyProject(member, 1L, nameDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto).isNotNull();
        assertThat(project.getName()).isEqualTo(nameDto.getName());
        printTime("프로젝트 수정 성공 테스트", stopWatch.getTotalTimeMillis());
    }
    
    @DisplayName("프로젝트 커버 사진 수정 실패")
    @Test
    void modifyProjectCoverFailed() {
        StopWatch stopWatch = new StopWatch();
        // given
        Member member = new Member(1L, null, UserRole.MEMBER, "DGSW");
        Project project = new Project(1L, null, "Flock", null);

        lenient().when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));
        lenient().when(teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class)))
                .thenThrow(new TeamMember.NoPermissionException());

        // when
        stopWatch.start();
        TeamMember.NoPermissionException noPermissionException
                = assertThrows(
                        TeamMember.NoPermissionException.class,
                        () -> projectService.modifyProjectImg(member, 1L, new ContentDto("https://이미지.jpg"))
        );
        stopWatch.stop();

        // then
        assertThat(noPermissionException.getHttpStatus())
                .isEqualTo(HttpStatus.FORBIDDEN);
        printTime("프로젝트 커버 사진 수정 실패", stopWatch.getTotalTimeMillis());
    }
    
    @DisplayName("프로젝트 커버 사진 수정 성공")
    @Test
    void modifyProjectCoverSuccess() {
        StopWatch stopWatch = new StopWatch();
        // given
        Member member = new Member(1L, null, UserRole.MEMBER, "DGSW");
        ContentDto contentDto = new ContentDto("https://이미지.jpg");
        Project project = new Project(1L, null, "Flock", null);
        TeamMember teamMember = new TeamMember(null, member, Role.Leader, true);

        lenient().when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));
        lenient().when(teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class)))
                .thenReturn(Optional.of(teamMember));

        // when
        stopWatch.start();
        LinkListDto linkListDto = projectService.modifyProjectImg(member, 1L, contentDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto).isNotNull();
        assertThat(project.getCoverImage()).isEqualTo(contentDto.getContent());
        printTime("프로젝트 커버 사진 수정 성공", stopWatch.getTotalTimeMillis());
    }
}
