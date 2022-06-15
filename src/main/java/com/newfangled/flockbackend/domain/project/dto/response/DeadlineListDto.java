package com.newfangled.flockbackend.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class DeadlineListDto {

    private List<TodoDto> todo;
}
