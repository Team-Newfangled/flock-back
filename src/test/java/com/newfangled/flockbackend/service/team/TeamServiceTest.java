package com.newfangled.flockbackend.service.team;

import com.newfangled.flockbackend.domain.member.embed.OAuth;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamMemberRO;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.repository.TeamRepository;
import com.newfangled.flockbackend.domain.team.service.TeamService;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TeamService teamService;

    private OAuth oAuth() {
        return OAuth.builder()
                .name("blah")
                .oauthId("1")
                .pictureUrl("picture")
                .build();
    }

    private Member teamMaker(OAuth oAuth) {
        return Member.builder()
                .id(1L)
                .oAuth(oAuth)
                .role(UserRole.MEMBER)
                .company("회사")
                .build();
    }

    private void printTime(long time) {
        System.out.printf("'서비스 소요 시간': %d ms%n", time);
    }

    @DisplayName("팀 만들기 테스트")
    @Test
    void createTeamTest() {
        StopWatch stopWatch = new StopWatch();
        // given
        NameDto nameDto = new NameDto("NewFangled");
        Team team = new Team(1L, nameDto.getName());
        ProjectDto projectDto = new ProjectDto(team.getId(), team.getName());

        lenient().when(teamRepository.save(any(Team.class)))
                .thenReturn(team);
        lenient().when(teamMemberRepository.save(any(TeamMember.class)))
                .thenReturn(null);
        // when
        stopWatch.start();
        TeamDto teamDto = teamService.createTeam(any(Member.class), nameDto);
        stopWatch.stop();

        // then
        assertThat(teamDto).isNotNull();
        assertThat(teamDto.getTeamName()).isEqualTo(projectDto.getName());
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("팀 프로젝트 조회")
    @Test
    void findAllProjects() {
        StopWatch stopWatch = new StopWatch();
        // given
        Team team = new Team(1L, "NewFangled");
        Project project = new Project(1L, team, "끼리끼리", null);
        Page<Project> projects = new PageImpl<>(List.of(project));

        lenient().when(teamRepository.findById(anyLong()))
                        .thenReturn(Optional.of(team));
            lenient().when(projectRepository.findAllByTeam(any(Team.class), any()))
                .thenReturn(projects);

        // when
        stopWatch.start();
        PageDto<ProjectDto> teamProjects = teamService.findAllTeamProjects(1L, eq(0));
        stopWatch.stop();

        // then
        assertThat(teamProjects).isNotNull();
        assertThat(teamProjects.getResults().size()).isEqualTo(1);
        assertThat(teamProjects.getPage()).isEqualTo(0);
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("팀원 퇴출")
    @Test
    void expulsionMember() {
        StopWatch stopWatch = new StopWatch();
        // given

        // 로직: 팀들 중에서 회원 id 를 delete
        Team team = new Team(1L, "NewFangled");
        TeamId teamId = new TeamId(team);
        Member member = teamMaker(oAuth());
        TeamMember teamMember = new TeamMember(teamId, member, Role.Leader, true);

        lenient().when(teamRepository.findById(anyLong()))
                        .thenReturn(Optional.of(team));
        lenient().when(teamMemberRepository.findByMember_IdAndTeamId(anyLong(), any(TeamId.class)))
                        .thenReturn(Optional.of(teamMember));

        // when
        stopWatch.start();
        teamService.expulsionMember(member, 1L, 1L);
        stopWatch.stop();

        verify(teamMemberRepository, times(1)).delete(any(TeamMember.class));
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("팀원 조회")
    @Test
    void findAllMembers() {
        StopWatch stopWatch = new StopWatch();
        // given
        Team team = new Team(1L, "NewFangled");
        TeamId teamId = new TeamId(team);
        Member member = teamMaker(oAuth());
        TeamMember teamMember = new TeamMember(teamId, member, Role.Member, true);
        Page<TeamMember> teamMembers = new PageImpl<>(List.of(teamMember));

        lenient().when(teamRepository.findById(anyLong()))
                .thenReturn(Optional.of(team));
        lenient().when(teamMemberRepository.findAllByTeamId(any(TeamId.class), any(Pageable.class)))
                .thenReturn(teamMembers);

        // when
        stopWatch.start();
        PageDto<TeamMemberRO> pageDto = teamService.findAllMember(1L, 0);
        stopWatch.stop();

        // then
        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getPage()).isEqualTo(0);
        assertThat(pageDto.getResults()).isNotNull();
        assertThat(pageDto.getResults().size()).isEqualTo(1);
        printTime(stopWatch.getTotalTimeMillis());

        // verify
        verify(teamMemberRepository, times(1)).findAllByTeamId(
                any(TeamId.class), any(Pageable.class));
    }
    
    @DisplayName("프로젝트 추가")
    @Test
    void addProject() {
        // given
        StopWatch stopWatch = new StopWatch();
        Member member = new Member(1L, null, UserRole.MEMBER, "NewFangeld");
        NameDto nameDto = new NameDto("Flock");
        Team team = new Team(1L, "NewFangled");
        Project project = new Project(1L, team, nameDto.getName(), null);

        lenient().when(teamRepository.findById(anyLong()))
                .thenReturn(Optional.of(team));
        lenient().when(projectRepository.save(any(Project.class)))
                .thenReturn(project);

        // when
        stopWatch.start();
        ProjectDto projectDto = teamService.createProject(member, 1L, nameDto);
        stopWatch.stop();
        
        // then
        assertThat(projectDto).isNotNull();
        assertThat(projectDto.getName()).isEqualTo(nameDto.getName());
        printTime(stopWatch.getTotalTimeMillis());
    }
}
