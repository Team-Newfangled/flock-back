package com.newfangled.flockbackend.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDto {

    private final long id;
    private final String name;

    @JsonProperty("cover_image")
    private final String coverImage;

    public ProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.coverImage = project.getCoverImage();
    }

}
