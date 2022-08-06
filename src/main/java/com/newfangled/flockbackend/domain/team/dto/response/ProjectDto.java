package com.newfangled.flockbackend.domain.team.dto.response;

import com.newfangled.flockbackend.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ProjectDto {

    private long id;
    private String name;

    public ProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
    }

}
