package com.newfangled.flockbackend.domain.project.controller;

import com.newfangled.flockbackend.domain.project.service.ProjectService;
import com.newfangled.flockbackend.domain.team.dto.response.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ProjectDto findProject(@PathVariable("id") long id) {
        return projectService.findProject(id);
    }
}
