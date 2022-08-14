package com.newfangled.flockbackend.controller.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newfangled.flockbackend.controller.ControllerTestUtil;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.domain.member.service.AuthDetailsService;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.team.controller.TeamController;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamMemberRO;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.repository.TeamRepository;
import com.newfangled.flockbackend.domain.team.service.TeamService;
import com.newfangled.flockbackend.global.config.jwt.JwtConfiguration;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@EnableAutoConfiguration
@EnableConfigurationProperties(JwtConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = TeamController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthenticationEntryPoint.class)
})
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthDetailsService authDetailsService;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private TeamService teamService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private TeamMemberRepository teamMemberRepository;

    @MockBean
    private ProjectRepository projectRepository;

    private Member member() {
        return Member.builder()
                .id(1L)
                .oAuth(null)
                .company("MyCompany")
                .role(UserRole.MEMBER)
                .build();
    }

    private TeamMemberRO randomTeamMember(long id) {
        String randomString = ControllerTestUtil.randomString();
        return new TeamMemberRO(
                id, randomString, "Member", true, String.format("/users/%s", randomString)
        );
    }

    private ProjectDto randomProject(long id) {
        return new ProjectDto(id, ControllerTestUtil.randomString());
    }

    @DisplayName("팀 만들기 테스트 성공")
    @Test
    void createTeamSuccess() throws Exception {
        // given
        Member member = member();
        NameDto nameDto = new NameDto("NewFangled");
        Team team = new Team(1L, nameDto.getName());
        TeamDto teamDto = new TeamDto(team.getId(), team.getName());
        String content = objectMapper.writeValueAsString(nameDto);

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(teamService.createTeam(any(Member.class), any(NameDto.class)))
                .thenReturn(teamDto);

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams",
                content, "post",
                ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.team-name").exists())
                .andExpect(jsonPath("$.team-name").value(nameDto.getName()));
    }

    @DisplayName("팀 프로젝트 조회 성공")
    @Test
    void findProjectsTestSuccess() throws Exception {
        // given
        Member member = member();
        PageDto<ProjectDto> pageDto = new PageDto<>(
                0, 1, List.of(
                        randomProject(1), randomProject(2), randomProject(3)
                )
        );

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(teamService.findAllTeamProjects(anyLong(), anyInt()))
                .thenReturn(pageDto);
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams/1/projects/?page=0",
                "", "get", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").exists())
                .andExpect(jsonPath("$.results.size()").value(3));
    }

    @DisplayName("팀 프로젝트 조회 실패")
    @Test
    void findProjectsTestFailed() throws Exception {
        // given
        Member member = member();

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(teamService.findAllTeamProjects(anyLong(), anyInt()))
                .thenThrow(new Team.NotExistsException());
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams/1/projects?page=0",
                null, "get", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }
    
    @DisplayName("팀원 퇴출 성공")
    @Test
    void expulsionMemberSuccess() throws Exception {
        // given
        Member member = member();

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().doNothing().when(teamService)
                .expulsionMember(any(), anyLong(), anyLong());
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams/1/expulsion/1",
                null, "delete", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isNoContent());
    }

    @DisplayName("팀원 퇴출 실패")
    @Test
    void expulsionMemberFailed() throws Exception {
        // given
        Member member = member();

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().doThrow(new TeamMember.NoPermissionException())
                .when(teamService).expulsionMember(any(), anyLong(), anyLong());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams/1/expulsion/1",
                null, "delete", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden());
    }
    
    @DisplayName("팀원 조회 성공")
    @Test
    void findMemberSuccess() throws Exception {
        // given
        Member member = member();
        PageDto<TeamMemberRO> teamMemberPage = new PageDto<>(
                0, 1, List.of(randomTeamMember(1), randomTeamMember(2))
        );

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(teamService.findAllMember(any(Member.class), anyLong(), anyInt()))
                .thenReturn(teamMemberPage);
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams/1/members?page=0",
                null, "get", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.results").exists());
    }
    
    @DisplayName("프로젝트 추가 성공")
    @Test
    void addProjectSuccess() throws Exception {
        // given
        Member member = member();
        NameDto nameDto = new NameDto(ControllerTestUtil.randomString());
        ProjectDto projectDto = new ProjectDto(1L, nameDto.getName());
        String content = objectMapper.writeValueAsString(nameDto);

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(teamService.createProject(any(Member.class), anyLong(), any(NameDto.class)))
                .thenReturn(projectDto);
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams/1/projects",
                content, "post", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(nameDto.getName()));
    }
    
    @DisplayName("프로젝트 추가 실패")
    @Test
    void addProjectFailed() throws Exception {
        // given
        Member member = member();
        NameDto nameDto = new NameDto(ControllerTestUtil.randomString());
        String content = objectMapper.writeValueAsString(nameDto);

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(teamService.createProject(any(Member.class), anyLong(), any(NameDto.class)))
                .thenThrow(new Team.NotExistsException());
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/teams/1/projects",
                content, "post", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
    
}
