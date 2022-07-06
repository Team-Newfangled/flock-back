package com.newfangled.flockbackend.domain.account.service;

import com.newfangled.flockbackend.domain.account.dto.response.ProfileDto;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.repository.AccountRepository;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public ProfileDto findAccountById(long accountId) {
        return new ProfileDto(findById(accountId));
    }

    public LinkListDto updateNickname(long accountId, NameDto nameDto) {
        Account account = findById(accountId);
        account.getOAuth().updateName(nameDto.getName());
        LinkDto linkDto = new LinkDto(
                "self",
                "GET",
                String.format("/users/%d", account.getId())
        );
        return new LinkListDto("닉네임을 변경하였습니다", List.of(linkDto));
    }

    public LinkListDto updatePicture(long accountId, ContentDto contentDto) {
        Account account = findById(accountId);
        account.getOAuth().updatePicture(contentDto.getContent());
        LinkDto linkDto = new LinkDto(
                "self",
                "GET",
                String.format("/users/%d", account.getId())
        );
        return new LinkListDto("사진을 변경하였습니다", List.of(linkDto));
    }

    @Transactional(readOnly = true)
    public NameDto findCompany(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(Account.NotExistsException::new);
        return new NameDto(account.getCompany());
    }

    public LinkListDto updateCompany(Account account, NameDto nameDto) {
        return null;
    }

    @Transactional(readOnly = true)
    protected Account findById(long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(Account.NotExistsException::new);
    }
}
