package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TeamId teamId;

    @Column(name = "project_name")
    private String name;
}
