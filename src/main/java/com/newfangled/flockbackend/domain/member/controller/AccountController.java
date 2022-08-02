package com.newfangled.flockbackend.domain.member.controller;

import com.newfangled.flockbackend.domain.member.dto.response.ProfileDto;
import com.newfangled.flockbackend.domain.member.service.AccountService;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.ResultListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users/{user-id}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ProfileDto findUserPicture(@PathVariable("user-id") long accountId) {
        return accountService.findAccountById(accountId);
    }

    @PatchMapping("/name")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto changeName(@PathVariable("user-id") long accountId,
                                  @RequestBody @Valid NameDto nameDto) {
        return accountService.updateNickname(accountId, nameDto);
    }

    @PatchMapping("/picture")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto changePicture(@PathVariable("user-id") long accountId,
                                     @RequestBody @Valid ContentDto contentDto) {
        return accountService.updatePicture(accountId, contentDto);
    }

    @GetMapping("/organization")
    public NameDto findOrganization(@PathVariable("user-id") long accountId) {
        return accountService.findCompany(accountId);
    }

    @PatchMapping("/organization")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto changeOrganization(@PathVariable("user-id") long accountId,
                                          @RequestBody @Valid NameDto nameDto) {
        return accountService.updateCompany(accountId, nameDto);
    }

    @GetMapping("/team")
    public ResultListDto<NameDto> findAllTeams(
            @PathVariable("user-id") long accountId) {
        return accountService.findAllTeams(accountId);
    }
}
