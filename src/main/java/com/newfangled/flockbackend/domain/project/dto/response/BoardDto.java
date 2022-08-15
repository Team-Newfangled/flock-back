package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.project.entity.sub.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class BoardDto {

    private long id;
    private String content;

    @JsonProperty("writer-id")
    private long writerId;

    private String file;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.content = board.getContent();
        this.writerId = board.getTeamMember().getTeamId().getMember().getId();
        this.file = board.getFile();
    }

}
