package com.newfangled.flockbackend.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FileLinkListDto {

    private long id;
    private String message;

    @JsonProperty("file_links")
    private List<FileDto> fileLinks;

}
