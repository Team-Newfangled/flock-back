package com.newfangled.flockbackend.domain.team.entity;

import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.embed.TeamId;
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

    @EmbeddedId
    private TeamId teamId;

    @OneToOne
    private Account account;

    @Enumerated(EnumType.STRING)
    private Role role;
}
