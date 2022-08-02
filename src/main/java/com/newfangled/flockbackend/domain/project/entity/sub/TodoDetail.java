package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    private LocalDate startDate;

    private LocalDate endDate;

}
