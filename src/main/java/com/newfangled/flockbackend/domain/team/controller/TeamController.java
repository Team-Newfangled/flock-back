package com.newfangled.flockbackend.domain.team.controller;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamDto;
import com.newfangled.flockbackend.domain.team.dto.response.TeamMemberRO;
import com.newfangled.flockbackend.domain.team.service.TeamService;
import com.newfangled.flockbackend.global.dto.NameDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto createTeam(Authentication authentication,
                              @RequestBody @Valid final NameDto nameDto) {
        return teamService.createTeam((Member) authentication.getPrincipal(), nameDto);
    }

    @GetMapping("{id}/projects")
    public PageDto<ProjectDto> findProjects(@PathVariable("id") long id,
                                            @RequestParam("page") int page) {
        return teamService.findAllTeamProjects(id, page);
    }

    @DeleteMapping("{id}/expulsion/{user-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void expulsionMember(Authentication authentication,
                                @PathVariable("id") long id,
                                @PathVariable("user-id") long userId) {
        teamService.expulsionMember((Member) authentication.getPrincipal(), id, userId);
    }

    @GetMapping("/{id}/members")
    public PageDto<TeamMemberRO> findMembers(Authentication authentication,
                                             @PathVariable("id") long id,
                                             @RequestParam int page) {
        return teamService.findAllMember(
                (Member) authentication.getPrincipal(), id, page
        );
    }

    @PostMapping("/{id}/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto addProject(Authentication authentication,
                                 @PathVariable("id") long id,
                                 @RequestBody @Valid final NameDto nameDto) {
        return teamService.createProject(
                (Member) authentication.getPrincipal(),
                id, nameDto
        );
    }
}
