package com.newfangled.flockbackend.global.embed;

import com.newfangled.flockbackend.domain.project.entity.Board;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Embeddable
public class BoardId implements Serializable {

    @ManyToOne
    private Board board;

}
