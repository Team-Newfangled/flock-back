package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class DeadLine {
    
    @EmbeddedId
    private TeamId teamId;

    @NotNull
    @Column(name = "deadline_date")
    private Date deadLineDate;

    @NotNull
    @Column(name = "startline_date")
    private Date startLineDate;

}
