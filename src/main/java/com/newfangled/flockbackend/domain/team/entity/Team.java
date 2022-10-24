package com.newfangled.flockbackend.domain.team.entity;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMember> teamIds = new HashSet<>();

    public static class NotExistsException extends BusinessException {
        public NotExistsException() {
            super(HttpStatus.NOT_FOUND, "팀을 찾지 못함");
        }
    }

}
