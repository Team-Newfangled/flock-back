package com.newfangled.flockbackend.domain.sns.entity;

import com.newfangled.flockbackend.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class SNSBoard {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    private String image;

    private String url;


}
