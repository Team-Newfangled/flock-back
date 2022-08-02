package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Project project;

    @OneToOne
    private TeamMember teamMember;

    private String content;

    private String file;

}
