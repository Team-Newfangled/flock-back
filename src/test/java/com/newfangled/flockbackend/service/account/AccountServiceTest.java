package com.newfangled.flockbackend.service.account;

import com.newfangled.flockbackend.domain.account.dto.response.ProfileDto;
import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.repository.AccountRepository;
import com.newfangled.flockbackend.domain.account.service.AccountService;
import com.newfangled.flockbackend.domain.account.type.UserRole;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.ResultListDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private AccountService accountService;

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

    private void printTime(long time) {
        System.out.printf("'????????? ?????? ??????': %d ms%n", time);
    }

    @DisplayName("????????? ??????")
    @RepeatedTest(20)
    void changeNickname() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        lenient().when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        NameDto nameDto = new NameDto(randomString());

        // when
        stopWatch.start();
        LinkListDto linkListDto = accountService
                .updateNickname(1L, nameDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto.getMessage()).isEqualTo("???????????? ?????????????????????");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getOAuth().getName()).isEqualTo(nameDto.getName());

        // verify
        verify(accountRepository, times(1))
                .findById(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("????????? ??????")
    @Test
    void getUserPicture() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        lenient().when(accountRepository.findById(anyLong()))
                        .thenReturn(Optional.of(account));

        // when
        stopWatch.start();
        ProfileDto profileDto = accountService.findAccountById(account.getId());
        stopWatch.stop();

        // then
        assertThat(profileDto).isNotNull();
        assertThat(profileDto.getCompany()).isEqualTo(account.getCompany());
        assertThat(profileDto.getImage()).isEqualTo(oAuth.getPictureUrl());
        assertThat(profileDto.getNickname()).isEqualTo(oAuth.getName());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("????????? ?????? ??????")
    @Test
    void changePicture() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        lenient().when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        ContentDto contentDto = new ContentDto(randomString());

        // when
        stopWatch.start();
        LinkListDto linkListDto = accountService
                .updatePicture(1L, contentDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto.getMessage()).isEqualTo("????????? ?????????????????????");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getOAuth().getPictureUrl()).isEqualTo(contentDto.getContent());

        // verify
        verify(accountRepository, times(1))
                .findById(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("????????? ?????? ??????")
    @Test
    void getCompanyName() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        lenient().when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        // when
        stopWatch.start();
        NameDto nameDto = accountService.findCompany(account.getId());
        stopWatch.stop();

        // then
        assertThat(nameDto).isNotNull();
        assertThat(nameDto.getName()).isEqualTo(account.getCompany());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("????????? ????????? ??????")
    @Test
    void changeCompany() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        lenient().when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        NameDto nameDto = new NameDto(randomString());

        // when
        stopWatch.start();
        LinkListDto linkListDto = accountService
                .updateCompany(1L, nameDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto.getMessage()).isEqualTo("???????????? ?????????????????????.");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getCompany()).isEqualTo(nameDto.getName());

        // verify
        verify(accountRepository, times(1))
                .findById(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }
    
    @DisplayName("????????? ??? ?????? ??????")
    @Test
    void findAllTeams() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        List<TeamMember> teamMemberList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            teamMemberList.add(new TeamMember(new TeamId(team(i, randomString())), account, Role.Leader));
        }
        lenient().when(teamMemberRepository.findDistinctByAccount_Id(anyLong()))
                .thenReturn(teamMemberList);

        // when
        stopWatch.start();
        ResultListDto<NameDto> companies = accountService.findAllTeams(1L);
        stopWatch.stop();

        // then
        assertThat(companies).isNotNull();
        assertThat(companies.getResults()).isNotEmpty();
        assertThat(companies.getResults().size()).isEqualTo(8);

        // verify
        verify(teamMemberRepository, times(1))
                .findDistinctByAccount_Id(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }
}
