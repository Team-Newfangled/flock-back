package com.newfangled.flockbackend.domain.project.embed;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Embeddable
public class TodoId implements Serializable {

    @ManyToOne
    private Project project;

    @OneToOne
    private TodoDetail todoDetail;

}
