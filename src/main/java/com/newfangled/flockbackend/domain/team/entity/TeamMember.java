package com.newfangled.flockbackend.domain.team.entity;

import com.newfangled.flockbackend.global.embed.TeamPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "team_member")
public class TeamMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TeamPosition teamPosition;

}
