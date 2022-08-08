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
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.service.TeamService;
import com.newfangled.flockbackend.global.config.jwt.JwtConfiguration;
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
}
