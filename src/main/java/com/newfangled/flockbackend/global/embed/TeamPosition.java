package com.newfangled.flockbackend.global.embed;

import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.type.Role;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
public class TeamPosition {

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
