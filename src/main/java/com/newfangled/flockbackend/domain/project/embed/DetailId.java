package com.newfangled.flockbackend.domain.project.embed;

import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class DetailId implements Serializable {

    @ManyToOne
    private Todo todo;

}
