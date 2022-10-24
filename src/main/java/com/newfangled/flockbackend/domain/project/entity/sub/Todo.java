package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.*;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private boolean completed;

    @Setter
    @OneToOne(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private TodoDetail todoDetail;

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public static class NotExistsException extends BusinessException {
        public NotExistsException() {
            super(HttpStatus.NOT_FOUND, "존재하지 않는 할 일 입니다.");
        }
    }

}
