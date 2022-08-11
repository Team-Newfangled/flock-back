package com.newfangled.flockbackend.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class TodoModifyDto {

    @Nullable
    private String content;

    @JsonProperty("start-date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    private LocalDate startDate;

    @JsonProperty("end-date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    private LocalDate endDate;

}
