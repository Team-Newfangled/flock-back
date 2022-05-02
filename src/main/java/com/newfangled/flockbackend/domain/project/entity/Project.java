package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Project {
    
    @EmbeddedId
    private TeamId teamId;

    @ManyToOne
    private TeamMember teamMember;

    @NotNull
    private String name;
    
}
