package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Project project;

    @OneToOne
    private Account account;

    private String content;

    @OneToMany
    @JoinColumn(name = "board_id")
    private Set<BoardFile> boardFileSet;

}
