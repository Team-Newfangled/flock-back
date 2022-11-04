package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@AllArgsConstructor @NoArgsConstructor
public class TodoDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private TeamMember teamMember;

    @Setter
    @OneToOne
    private Todo todo;

    private String content;

    private String color;

    @DateTimeFormat(pattern = "yyyy-MM")
    private LocalDate startDate;

    private LocalDate endDate;

    @Setter
    @Builder.Default
    private short percent = 0;

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
