package com.newfangled.flockbackend.service.account;

import com.newfangled.flockbackend.domain.member.dto.response.ProfileDto;
import com.newfangled.flockbackend.domain.member.embed.OAuth;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.domain.member.service.AccountService;
import com.newfangled.flockbackend.domain.member.type.UserRole;
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
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

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

    private void printTime(long time) {
        System.out.printf("'서비스 소요 시간': %d ms%n", time);
    }

    @DisplayName("닉네임 변경")
    @RepeatedTest(20)
    void changeNickname() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        lenient().when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        NameDto nameDto = new NameDto(randomString());

        // when
        stopWatch.start();
        LinkListDto linkListDto = accountService
                .updateNickname(1L, nameDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto.getMessage()).isEqualTo("닉네임을 변경하였습니다");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getOAuth().getName()).isEqualTo(nameDto.getName());

        // verify
        verify(memberRepository, times(1))
                .findById(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("프로필 조회")
    @Test
    void getUserPicture() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        lenient().when(memberRepository.findById(anyLong()))
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

    @DisplayName("사용자 사진 변경")
    @Test
    void changePicture() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        lenient().when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        ContentDto contentDto = new ContentDto(randomString());

        // when
        stopWatch.start();
        LinkListDto linkListDto = accountService
                .updatePicture(1L, contentDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto.getMessage()).isEqualTo("사진을 변경하였습니다");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getOAuth().getPictureUrl()).isEqualTo(contentDto.getContent());

        // verify
        verify(memberRepository, times(1))
                .findById(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }

    @DisplayName("사용자 회사 조회")
    @Test
    void getCompanyName() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        lenient().when(memberRepository.findById(anyLong()))
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

    @DisplayName("사용자 회사명 변경")
    @Test
    void changeCompany() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        lenient().when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        NameDto nameDto = new NameDto(randomString());

        // when
        stopWatch.start();
        LinkListDto linkListDto = accountService
                .updateCompany(1L, nameDto);
        stopWatch.stop();

        // then
        assertThat(linkListDto.getMessage()).isEqualTo("회사명을 수정하였습니다.");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getCompany()).isEqualTo(nameDto.getName());

        // verify
        verify(memberRepository, times(1))
                .findById(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }
    
    @DisplayName("사용자 팀 전체 조회")
    @Test
    void findAllTeams() {
        StopWatch stopWatch = new StopWatch();
        // given
        OAuth oAuth = oAuth();
        Member account = account(oAuth);
        List<TeamMember> teamMemberList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            teamMemberList.add(new TeamMember(new TeamId(team(i, randomString())), account, Role.Leader));
        }
        lenient().when(teamMemberRepository.findDistinctByMember_Id(anyLong()))
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
                .findDistinctByMember_Id(anyLong());

        // finally
        printTime(stopWatch.getTotalTimeMillis());
    }
}
