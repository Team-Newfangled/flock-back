package com.newfangled.flockbackend.global.embed;

import com.newfangled.flockbackend.domain.team.entity.Team;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Embeddable
public class TeamId implements Serializable {

    @OneToOne
    private Team team;

}
