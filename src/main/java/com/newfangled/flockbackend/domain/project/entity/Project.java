package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.global.embed.TeamId;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Team team;

    @Column(name = "project_name")
    private String name;

    public void setTeam(Team team) {
        this.team = team;
    }

    public static class NotExistsException extends BusinessException {
        public NotExistsException() {
            super(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다.");
        }
    }
}
