package com.newfangled.flockbackend.domain.team.entity;

import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.team.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "team_member")
public class TeamMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    private Team team;

    @OneToOne
    @JoinColumn
    private Account account;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'MEMBER'")
    private Role role;

}
