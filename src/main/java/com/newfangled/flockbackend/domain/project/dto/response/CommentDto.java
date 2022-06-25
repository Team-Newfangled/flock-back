package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class CommentDto {

    private long id;
    private String comment;

    @JsonProperty("comment-id")
    private long commentId;

    @JsonProperty("writer-id")
    private long writerId;

}
