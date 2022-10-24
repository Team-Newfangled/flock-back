package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Project project;

    @OneToOne
    private TeamMember teamMember;

    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<BoardComment> boardComments = new HashSet<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<BoardFile> boardFiles = new HashSet<>();

    public void modifyContent(String content) {
        this.content = content;
    }

    public static class NotExistsException extends BusinessException {
        public NotExistsException() {
            super(HttpStatus.NOT_FOUND, "피드를 찾지 못하였습니다.");
        }
    }

}
