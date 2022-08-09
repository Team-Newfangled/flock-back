package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.project.embed.TodoId;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class TodoDto {

    private long id;
    private String content;
    private String color;

    @JsonProperty("writer_id")
    private long writerId;

    @JsonProperty("start-date")
    private LocalDate startDate;

    @JsonProperty("end-date")
    private LocalDate endDate;

    public TodoDto(Todo todo) {
        this.id = todo.getId();
        TodoId todoId = todo.getTodoId();
        TodoDetail todoDetail = todoId.getTodoDetail();
        this.content = todoDetail.getContent();
        this.color = todoDetail.getColor();
        this.writerId = todoDetail.getTeamMember().getMember().getId();
        this.startDate = todoDetail.getStartDate();
        this.endDate = todoDetail.getEndDate();
    }

}
