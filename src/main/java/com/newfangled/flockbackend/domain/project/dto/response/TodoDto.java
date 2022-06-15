package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

}
