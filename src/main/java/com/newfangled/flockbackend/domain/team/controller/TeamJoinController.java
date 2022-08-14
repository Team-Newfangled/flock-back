package com.newfangled.flockbackend.domain.team.controller;

import com.newfangled.flockbackend.domain.member.service.AccountService;
import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import com.newfangled.flockbackend.domain.team.service.TeamService;
import com.newfangled.flockbackend.global.exception.BusinessException;
import com.newfangled.flockbackend.global.infra.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TeamJoinController {

    private final TeamService teamService;
    private final AccountService accountService;
    private final MailService mailService;

    @PostMapping("/teams/{id}/join-mail")
    public String sendJoinMail(@PathVariable("id") long id, String email) {
        // 이메일 전송 로직
        if (!mailService.isValidEmailAddress(email)) {
            throw new BusinessException(HttpStatus.CONFLICT, "유효한 이메일이 아닙니다.");
        }
        TeamDto teamDto = teamService.findTeamById(id);
        long memberId = accountService.findByEmail(email);
        mailService.sendMail(teamDto.getTeamName(), id, email, memberId);

        // 전송완료 html
        return "redirect:/join-mail";
    }

    @GetMapping("/join-mail")
    public String joinMail() {
        return "emailSend";
    }

    @PostMapping("/teams/{id}/join")
    public String joinMember(@PathVariable("id") long id, long memberId) {
        // 가입 완료
        boolean isSuccess = teamService.joinMember(id, memberId);

        // 가입 완료 html
        return "redirect:/join-member?success=" + isSuccess;
    }

    @GetMapping("/join-member")
    public String joinComplete(@RequestParam Boolean success) {
        return (success) ? "success" : "failed";
    }

    @GetMapping("/teams/{id}/join")
    public String join(@PathVariable("id") long id, Model model) {
        TeamDto teamDto = teamService.findTeamById(id);
        model.addAttribute("team", teamDto.getTeamName());
        model.addAttribute("id", id);
        return "joinTemplate";
    }
}
