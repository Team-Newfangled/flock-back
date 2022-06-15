package com.newfangled.flockbackend.domain.project.entity;

import com.newfangled.flockbackend.global.embed.BoardId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class BoardFile {

    @EmbeddedId
    private BoardId boardId;

    private String file;

}
