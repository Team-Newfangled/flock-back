package com.newfangled.flockbackend.global.embed;

import com.newfangled.flockbackend.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class TeamId implements Serializable {

    @ManyToOne
    private Team team;

}
