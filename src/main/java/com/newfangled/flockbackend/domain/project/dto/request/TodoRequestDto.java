package com.newfangled.flockbackend.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class TodoRequestDto {

    private String content;

    @JsonProperty("start-date")
    private LocalDate startDate;

    @JsonProperty("end-date")
    private LocalDate endDate;

}
