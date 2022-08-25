package com.newfangled.flockbackend.domain.project.dto.response;

import com.newfangled.flockbackend.domain.project.entity.sub.BoardFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDto {

    private final long id;
    private final String file;

    public FileDto(BoardFile boardFile) {
        this.id = boardFile.getId();
        this.file = boardFile.getFile();
    }

}
