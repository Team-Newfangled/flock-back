package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class BoardComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @OneToOne
    private TeamMember teamMember;
    
    private String content;

    public static class NotExistsException extends BusinessException {
        public NotExistsException() {
            super(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");
        }
    }

}
