package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Progress {

    @EmbeddedId
    private TeamId teamId;

    @Column(columnDefinition = "TINYINT default 0")
    private byte progress;

}
