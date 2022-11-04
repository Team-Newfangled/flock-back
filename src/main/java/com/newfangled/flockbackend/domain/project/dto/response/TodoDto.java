package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("manager")
    private long managerId;

    @JsonProperty("start-date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonProperty("end-date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    private boolean completed;

    private short percent;

    public TodoDto(Todo todo) {
        this.id = todo.getId();
        TodoDetail todoDetail = todo.getTodoDetail();
        this.content = todoDetail.getContent();
        this.color = todoDetail.getColor();
        this.managerId = todoDetail.getTeamMember().getMember().getId();
        this.startDate = todoDetail.getStartDate();
        this.endDate = todoDetail.getEndDate();
        this.completed = todo.isCompleted();
        this.percent = todoDetail.getPercent();
    }
}
