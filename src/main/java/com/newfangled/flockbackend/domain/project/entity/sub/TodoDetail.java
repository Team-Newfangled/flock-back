package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class TodoDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TeamMember teamMember;

    private String content;

    private String color;

    @CreationTimestamp
    private LocalDate startDate;

    private LocalDate endDate;

    public void modifyDetail(String content, LocalDate startDate,
                             LocalDate endDate) {
        this.content = content;
        this.startDate = (startDate == null) ? this.startDate : startDate;
        this.endDate = (endDate == null) ? this.endDate : endDate;
    }
}
