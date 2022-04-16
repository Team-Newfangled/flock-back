package com.newfangled.flockbackend.domain.sns.entity;

import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Follower {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account ;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
