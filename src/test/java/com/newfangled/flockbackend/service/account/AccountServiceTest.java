package com.newfangled.flockbackend.service.account;

import com.newfangled.flockbackend.domain.account.dto.request.AccountDto;
import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.repository.AccountRepository;
import com.newfangled.flockbackend.domain.account.service.AccountService;
import com.newfangled.flockbackend.domain.account.type.UserRole;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @DisplayName("닉네임 변경")
    @Test
    void changeNickname() {
        // given
        OAuth oAuth = oAuth();
        Account account = account(oAuth);
        lenient().when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        AccountDto accountDto = new AccountDto("해피해피");

        // when
        LinkListDto linkListDto = accountService
                .updateNickname(1L, accountDto);

        // then
        assertThat(linkListDto.getMessage()).isEqualTo("닉네임을 변경하였습니다");
        assertThat(linkListDto.getLinks().size()).isEqualTo(1);
        assertThat(account.getOAuth().getName()).isEqualTo("해피해피");

        // verify
        verify(accountRepository, timeout(20)).findById(anyLong());
    }
}