package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.project.entity.sub.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class CommentDto {

    private long id;
    private String comment;

    @JsonProperty("board-id")
    private long boardId;

    @JsonProperty("writer-id")
    private long writerId;

    public CommentDto(BoardComment boardComment) {
        this.id = boardComment.getId();
        this.comment = boardComment.getContent();
        this.boardId = boardComment.getBoard().getId();
        this.writerId = boardComment.getTeamMember().getMember().getId();
    }
}
