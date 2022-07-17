package com.newfangled.flockbackend.service.team;

import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.type.UserRole;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.repository.TeamRepository;
import com.newfangled.flockbackend.domain.team.service.TeamService;
import com.newfangled.flockbackend.global.dto.NameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private TeamService teamService;

    private OAuth oAuth() {
        return OAuth.builder()
                .name("blah")
                .oauthId("1")
                .pictureUrl("picture")
                .build();
    }

    private Account teamMaker(OAuth oAuth) {
        return Account.builder()
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
        ProjectDto projectDto1 = teamService.createTeam(any(Account.class), nameDto);
        stopWatch.stop();

        // then
        assertThat(projectDto1).isNotNull();
        assertThat(projectDto1.getName()).isEqualTo(projectDto.getName());
    }
}
