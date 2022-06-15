package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Project project;

    @OneToOne
    private Account account;

    @NotNull
    private String content;

    @NotNull
    private String color;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public void updateStartDate(LocalDate date) {
        this.startDate = date;
    }

    public void updateEndDate(LocalDate date) {
        this.endDate = date;
    }

}
