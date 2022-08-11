package com.newfangled.flockbackend.domain.project.entity.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.newfangled.flockbackend.domain.project.embed.DetailId;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class TodoDetail {

    @JsonIgnoreProperties
    @EmbeddedId
    private DetailId detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamMember teamMember;

    private String content;

    private String color;

    @DateTimeFormat(pattern = "yyyy-MM")
    private LocalDate startDate;

    private LocalDate endDate;

    public void modifyDetail(String content, LocalDate startDate,
                             LocalDate endDate) {
        this.content = (content == null) ? this.content : content;
        this.startDate = (startDate == null) ? this.startDate : startDate;
        this.endDate = (endDate == null) ? this.endDate : endDate;
    }

    public void modifyColor(String color) {
        this.color = color;
    }
}
