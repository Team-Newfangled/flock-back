package com.newfangled.flockbackend.global.embed;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TeamId implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Team team;

    @OneToOne
    private Member member;

}
