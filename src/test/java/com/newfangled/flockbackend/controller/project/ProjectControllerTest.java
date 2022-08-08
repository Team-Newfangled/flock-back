package com.newfangled.flockbackend.controller.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newfangled.flockbackend.controller.ControllerTestUtil;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.domain.member.service.AuthDetailsService;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.domain.project.controller.ProjectController;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.service.ProjectService;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.service.TeamService;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.config.jwt.JwtConfiguration;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import com.newfangled.flockbackend.global.infra.UriValidator;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@EnableAutoConfiguration
@EnableConfigurationProperties(JwtConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = ProjectController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthenticationEntryPoint.class)
})
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthDetailsService authDetailsService;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private TeamMemberRepository teamMemberRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @SpyBean
    private UriValidator uriValidator;

    private Member member() {
        return Member.builder()
                .id(1L)
                .oAuth(null)
                .company("MyCompany")
                .role(UserRole.MEMBER)
                .build();
    }

    @DisplayName("프로젝트 조회 실패")
    @Test
    void findProjectFailed() throws Exception {
        // given
        Member member = member();

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(projectService.findProject(anyLong()))
                .thenThrow(new Project.NotExistsException());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1",
                null, "get", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
    
    @DisplayName("프로젝트 조회 성공")
    @Test
    void findProjectSuccess() throws Exception {
        // given
        Member member = member();
        ProjectDto projectDto = new ProjectDto(1L, "Flock");

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(projectService.findProject(anyLong()))
                .thenReturn(projectDto);
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1",
                null, "get", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("프로젝트 삭제 실패")
    @Test
    void deleteProjectFailed() throws Exception {
        // given
        Member member = member();

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().doThrow(new TeamMember.NoPermissionException())
                .when(projectService).deleteProject(any(Member.class), anyLong());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1",
                null, "delete", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("프로젝트 삭제 성공")
    @Test
    void deleteProjectSuccess() throws Exception {
        // given
        Member member = member();

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().doNothing().when(projectService)
                .deleteProject(any(Member.class), anyLong());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1",
                null, "delete", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("프로젝트명 수정 실패")
    @Test
    void modifyProjectNameFailed() throws Exception {
        // given
        Member member = member();
        String content = objectMapper.writeValueAsString(new NameDto());

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(projectService.modifyProject(
                any(Member.class), anyLong(), any(NameDto.class))
        ).thenThrow(new TeamMember.NoPermissionException());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1",
                content, "patch", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isForbidden());
    }
    
    @DisplayName("프로젝트명 수정 성공")
    @Test
    void modifyProjectNameSuccess() throws Exception {
        // given
        Member member = member();
        LinkDto linkDto = new LinkDto("self", "GET", "/projects/1");
        LinkListDto linkListDto
                = new LinkListDto("팀명을 변경하였습니다.", List.of(linkDto)
        );
        NameDto nameDto = new NameDto("끼리끼리");
        String content = objectMapper.writeValueAsString(nameDto);

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(projectService.modifyProject(
                any(Member.class), anyLong(), any(NameDto.class))
        ).thenReturn(linkListDto);
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1",
                content, "patch", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("프로젝트 커버 사진 수정 실패")
    @Test
    void modifyProjectCoverFailed() throws Exception {
        // given
        Member member = member();
        String content = objectMapper
                .writeValueAsString(new ContentDto("https://image.jpg"));

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(projectService.modifyProjectImg(
                any(Member.class), anyLong(), any(ContentDto.class))
        ).thenThrow(new TeamMember.NoPermissionException());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1/image",
                content, "patch", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("프로젝트 커버 사진 수정 성공")
    @Test
    void modifyProjectCoverSuccess() throws Exception {
        // given
        Member member = member();
        Team team = new Team(1L, "NewFangled");
        Project project = new Project(1L, null, "Flock", null);
        TeamMember teamMember = new TeamMember(new TeamId(team), member, Role.Leader);
        ContentDto contentDto = new ContentDto("https://image.png");
        String content = objectMapper.writeValueAsString(contentDto);

        ControllerTestUtil.authenticateStumpMember(member, memberRepository);
        lenient().when(projectService.modifyProjectImg(
                any(Member.class), anyLong(), any(ContentDto.class))
        ).thenReturn(new LinkListDto("프로젝트의 커버 사진을 변경하였습니다.", List.of(new LinkDto())));

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/projects/1/image",
                content, "patch", ControllerTestUtil.getAccessToken(jwtTokenProvider)
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated());
    }
}
