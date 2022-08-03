package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class BoardComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @OneToOne
    private TeamMember teamMember;
    
    private String content;

}
