package com.newfangled.flockbackend.domain.project.embed;

import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Embeddable
public class TeamId implements Serializable {

    @ManyToOne
    private Team team;

}
