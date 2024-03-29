package com.newfangled.flockbackend.domain.member.service;

import com.newfangled.flockbackend.domain.member.dto.response.ProfileDto;
import com.newfangled.flockbackend.domain.member.dto.response.TeamListDto;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;

    public ProfileDto findAccountById(long accountId) {
        return new ProfileDto(findById(accountId));
    }

    public LinkListDto updateNickname(long accountId, NameDto nameDto) {
        Member account = findById(accountId);
        account.getOAuth().updateName(nameDto.getName());
        LinkDto linkDto = new LinkDto(
                "self",
                "GET",
                String.format("/users/%d", account.getId())
        );
        return new LinkListDto("닉네임을 변경하였습니다", List.of(linkDto));
    }

    public LinkListDto updatePicture(long accountId, ContentDto contentDto) {
        Member account = findById(accountId);
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
        Member account = memberRepository.findById(accountId)
                .orElseThrow(Member.NotExistsException::new);
        return new NameDto(account.getCompany());
    }

    public LinkListDto updateCompany(long accountId, NameDto nameDto) {
        Member account = findById(accountId);
        account.updateCompany(nameDto.getName());
        LinkDto linkDto = new LinkDto(
                "self",
                "GET",
                String.format("/users/%d", account.getId())
        );
        return new LinkListDto("회사명을 수정하였습니다.", List.of(linkDto));
    }

    public TeamListDto findAllTeams(long accountId) {
        List<TeamDto> teams = teamMemberRepository
                .findDistinctByTeamId_Member_Id(accountId)
                .stream()
                .map(TeamMember::getTeam)
                .map(TeamDto::new)
                .collect(Collectors.toList());
        return new TeamListDto(teams);
    }

    @Transactional(readOnly = true)
    public long findByEmail(String email) {
        return memberRepository.findByOAuth_Email(email)
                .orElseThrow(Member.NotExistsException::new)
                .getId();
    }

    @Transactional(readOnly = true)
    protected Member findById(long accountId) {
        return memberRepository.findById(accountId)
                .orElseThrow(Member.NotExistsException::new);
    }
}
