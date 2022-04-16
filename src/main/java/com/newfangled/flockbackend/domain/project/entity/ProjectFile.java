package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class ProjectFile {

    @EmbeddedId
    private TeamId teamId;

    @NotNull
    @Column(name = "file_name")
    private String fileName;

}
