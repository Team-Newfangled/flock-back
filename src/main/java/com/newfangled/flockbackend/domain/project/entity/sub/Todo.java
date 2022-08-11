package com.newfangled.flockbackend.domain.project.entity.sub;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newfangled.flockbackend.domain.project.embed.TodoId;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @JsonIgnore
    private TodoId todoId;

    private boolean completed;

    public void setTodoId(TodoId todoId) {
        this.todoId = todoId;
    }

    public void setCompleted() {
        this.completed = true;
    }

    public static class NotExistsException extends BusinessException {
        public NotExistsException() {
            super(HttpStatus.NOT_FOUND, "존재하지 않는 할 일 입니다.");
        }
    }

}
