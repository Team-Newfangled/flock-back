package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class BoardDto {

    private long id;
    private String content;

    @JsonProperty("board-id")
    private long boardId;

    @JsonProperty("writer-id")
    private long writerId;

    private List<FileDto> files;

}
