package com.newfangled.flockbackend.service.account;

import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.repository.AccountRepository;
import com.newfangled.flockbackend.domain.account.service.AccountService;
import com.newfangled.flockbackend.domain.account.type.UserRole;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

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
                .company("공돌이네")
                .role(UserRole.MEMBER)
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
        assertThat(linkListDto.getMessage()).isEqualTo("닉네임을 변경하였습니다");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getOAuth().getName()).isEqualTo(nameDto.getName());

        // verify
        verify(accountRepository, times(1))
                .findById(anyLong());
        printTime(stopWatch.getTotalTimeMillis());
    }
}
