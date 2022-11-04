package com.newfangled.flockbackend.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import lombok.Getter;

@Getter
public class MemberTodoRO {

    @JsonProperty("todo_content")
    private final String todoContent;

    public MemberTodoRO(TodoDetail todoDetail) {
        this.todoContent = todoDetail.getContent();
    }
}
