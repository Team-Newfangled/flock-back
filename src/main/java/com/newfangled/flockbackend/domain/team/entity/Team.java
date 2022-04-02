package com.newfangled.flockbackend.domain.team.entity;

import com.newfangled.flockbackend.domain.account.entity.Account;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Account member;

    @OneToOne
    @JoinColumn(name = "leader_id")
    private Account leader;

}
