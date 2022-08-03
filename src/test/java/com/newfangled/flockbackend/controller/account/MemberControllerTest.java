package com.newfangled.flockbackend.controller.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newfangled.flockbackend.domain.member.controller.AccountController;
import com.newfangled.flockbackend.domain.member.dto.response.ProfileDto;
import com.newfangled.flockbackend.domain.member.embed.OAuth;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.AccountRepository;
import com.newfangled.flockbackend.domain.member.service.AccountService;
import com.newfangled.flockbackend.domain.member.service.AuthDetailsService;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.config.jwt.JwtConfiguration;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.ResultListDto;
import com.newfangled.flockbackend.global.embed.TeamId;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@EnableAutoConfiguration
@EnableConfigurationProperties(JwtConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = AccountController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthenticationEntryPoint.class)
})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthDetailsService authDetailsService;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TeamMemberRepository teamMemberRepository;

    private OAuth oAuth() {
        return OAuth.builder()
                .name("blah")
                .oauthId("1")
                .pictureUrl("picture")
                .build();
    }

    private Member account(OAuth oAuth) {
        return Member.builder()
                .id(1L)
                .oAuth(oAuth)
                .company("공돌이네")
                .role(UserRole.MEMBER)
                .build();
    }

    private Team team(long id, String name) {
        return Team.builder()
                .id(id)
                .name(name)
                .build();
    }

    private String randomString() {
        return new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                )
                .toString();
    }

    private LinkListDto linkListDto(String topic) {
        return new LinkListDto(String.format("%s을 수정하였습니다.", topic),
                List.of(new LinkDto("self", "GET", "/users/1"))
        );
    }

    private void stumpAccount(Member account) {
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
    }

    private String token() {
        return String.format("Bearer %s", jwtTokenProvider.generateAccessToken("1"));
    }

    private String fakeToken() {
        return "Bearer NULL";
    }
    
    @DisplayName("사용자 닉네임 변경 성공")
    @Test
    void changeNicknameSuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        NameDto nameDto = new NameDto(randomString());
        LinkListDto linkListDto = linkListDto("닉네임");
        String content = objectMapper.writeValueAsString(nameDto);

        stumpAccount(account);
        lenient().when(accountService.updateNickname(anyLong(), any(NameDto.class)))
                .thenReturn(linkListDto);
    
        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1/name",
                content, "patch", token()
        );
        
        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated());
    }
    
    @DisplayName("사용자 닉네임 변경 실패")
    @Test
    void changeNicknameFailed() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        NameDto nameDto = new NameDto(randomString());
        String content = objectMapper.writeValueAsString(nameDto);

        stumpAccount(account);
        lenient().when(accountService.updateNickname(anyLong(), any(NameDto.class)))
                .thenThrow(new Member.UnauthorizedException());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1/name",
                content, "patch", fakeToken()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("사용자 프로필 조회 성공")
    @Test
    void findUserPictureSuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        ProfileDto profileDto = new ProfileDto(account);

        stumpAccount(account);
        lenient().when(accountService.findAccountById(anyLong()))
                .thenReturn(profileDto);

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1",
                "", "GET", token()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname", oAuth.getName()).exists())
                .andExpect(jsonPath("$.image", oAuth.getPictureUrl()).exists());
    }

    @DisplayName("사용자 프로필 조회 실패")
    @Test
    void findUserPictureFailed() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);

        stumpAccount(account);
        lenient().when(accountService.findAccountById(anyLong()))
                .thenThrow(new Member.UnauthorizedException());

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1",
                "", "GET", fakeToken()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized());
    }
    
    @DisplayName("사용자 사진 변경 성공")
    @Test
    void changeUserPictureSuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        ContentDto contentDto = new ContentDto(randomString() + ".png");
        LinkListDto linkListDto = linkListDto("사진");
        String content = objectMapper.writeValueAsString(contentDto);

        stumpAccount(account);
        lenient().when(accountService.updatePicture(anyLong(), any(ContentDto.class)))
                .thenReturn(linkListDto);

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1/picture",
                content, "PATCH", token()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("사용자 회사 조회 성공")
    @Test
    void findCompanySuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        NameDto nameDto = new NameDto(randomString());

        stumpAccount(account);
        lenient().when(accountService.findCompany(anyLong()))
                .thenReturn(nameDto);

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1/organization",
                "", "get", token()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("사용자 회사명 변경 성공")
    @Test
    void changeCompanySuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        NameDto nameDto = new NameDto(randomString());
        LinkListDto linkListDto = linkListDto("회사명");
        String content = objectMapper.writeValueAsString(nameDto);

        stumpAccount(account);
        lenient().when(accountService.updateNickname(anyLong(), any(NameDto.class)))
                .thenReturn(linkListDto);

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1/organization",
                content, "patch", token()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("사용자 팀 전체 조회 성공")
    @Test
    void findAllTeamsSuccess() throws Exception {
        // given
        Member account = account(oAuth());
        List<TeamMember> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(new TeamMember(new TeamId(team(i, randomString())), account, Role.Leader));
        }
        List<NameDto> dtoList = results.stream()
                .map(TeamMember::getAccount)
                .map(Member::getCompany)
                .map(NameDto::new)
                .collect(Collectors.toList());
        ResultListDto<NameDto> listDto = new ResultListDto<>(dtoList);

        stumpAccount(account);
        lenient().when(accountService.findAllTeams(anyLong()))
                .thenReturn(listDto);

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1/team",
                "", "GET", token()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("사용자 팀 전체 조회 실패")
    @Test
    void finaAllTeamsFailed() throws Exception {
        // given
        Member account = account(oAuth());
        List<TeamMember> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(new TeamMember(new TeamId(team(i, randomString())), account, Role.Leader));
        }
        List<NameDto> dtoList = results.stream()
                .map(TeamMember::getAccount)
                .map(Member::getCompany)
                .map(NameDto::new)
                .collect(Collectors.toList());
        ResultListDto<NameDto> listDto = new ResultListDto<>(dtoList);

        stumpAccount(account);
        lenient().when(accountService.findAllTeams(anyLong()))
                .thenReturn(listDto);

        // when
        ResultActions resultActions = ControllerTestUtil.resultActions(
                mockMvc, "/users/1/team",
                "", "GET", fakeToken()
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}