package com.newfangled.flockbackend.domain.project.controller;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.service.ProjectService;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ProjectDto findProject(@PathVariable("id") long id) {
        return projectService.findProject(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(Authentication authentication,
                              @PathVariable("id") long id) {
        projectService.deleteProject((Member) authentication.getPrincipal(), id);
    }
}
