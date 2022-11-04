package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.project.entity.sub.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class BoardDto {

    private long id;
    private String content;

    @JsonProperty("writer-id")
    private long writerId;

    private String writer;

    private List<FileDto> files;

    public BoardDto(Board board, List<FileDto> files) {
        this.id = board.getId();
        this.content = board.getContent();
        this.writerId = board.getTeamMember().getMember().getId();
        this.writer = board.getTeamMember().getMember().getOAuth().getName();
        this.files = (files == null || files.size() == 0)
                ? new LinkedList<>()
                : files;
    }

}
