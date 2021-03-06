package com.newfangled.flockbackend.controller.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newfangled.flockbackend.domain.account.controller.AccountController;
import com.newfangled.flockbackend.domain.account.dto.response.ProfileDto;
import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.repository.AccountRepository;
import com.newfangled.flockbackend.domain.account.service.AccountService;
import com.newfangled.flockbackend.domain.account.service.AuthDetailsService;
import com.newfangled.flockbackend.domain.account.type.UserRole;
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
class AccountControllerTest {

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

    private Account account(OAuth oAuth) {
        return Account.builder()
                .id(1L)
                .oAuth(oAuth)
                .company("????????????")
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
        return new LinkListDto(String.format("%s??? ?????????????????????.", topic),
                List.of(new LinkDto("self", "GET", "/users/1"))
        );
    }

    private void stumpAccount(Account account) {
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
    }

    private String token() {
        return String.format("Bearer %s", jwtTokenProvider.generateAccessToken("1"));
    }

    private String fakeToken() {
        return "Bearer NULL";
    }
    
    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void changeNicknameSuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        NameDto nameDto = new NameDto(randomString());
        LinkListDto linkListDto = linkListDto("?????????");
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
    
    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void changeNicknameFailed() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        NameDto nameDto = new NameDto(randomString());
        String content = objectMapper.writeValueAsString(nameDto);

        stumpAccount(account);
        lenient().when(accountService.updateNickname(anyLong(), any(NameDto.class)))
                .thenThrow(new Account.UnauthorizedException());

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

    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void findUserPictureSuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
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

    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void findUserPictureFailed() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);

        stumpAccount(account);
        lenient().when(accountService.findAccountById(anyLong()))
                .thenThrow(new Account.UnauthorizedException());

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
    
    @DisplayName("????????? ?????? ?????? ??????")
    @Test
    void changeUserPictureSuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        ContentDto contentDto = new ContentDto(randomString() + ".png");
        LinkListDto linkListDto = linkListDto("??????");
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

    @DisplayName("????????? ?????? ?????? ??????")
    @Test
    void findCompanySuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
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

    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void changeCompanySuccess() throws Exception {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        NameDto nameDto = new NameDto(randomString());
        LinkListDto linkListDto = linkListDto("?????????");
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

    @DisplayName("????????? ??? ?????? ?????? ??????")
    @Test
    void findAllTeamsSuccess() throws Exception {
        // given
        Account account = account(oAuth());
        List<TeamMember> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(new TeamMember(new TeamId(team(i, randomString())), account, Role.Leader));
        }
        List<NameDto> dtoList = results.stream()
                .map(TeamMember::getAccount)
                .map(Account::getCompany)
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

    @DisplayName("????????? ??? ?????? ?????? ??????")
    @Test
    void finaAllTeamsFailed() throws Exception {
        // given
        Account account = account(oAuth());
        List<TeamMember> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(new TeamMember(new TeamId(team(i, randomString())), account, Role.Leader));
        }
        List<NameDto> dtoList = results.stream()
                .map(TeamMember::getAccount)
                .map(Account::getCompany)
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
