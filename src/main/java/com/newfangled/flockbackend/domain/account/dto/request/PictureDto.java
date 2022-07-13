package com.newfangled.flockbackend.domain.account.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class PictureDto {

    @NotNull
    @JsonProperty("image_url")
    private String imageUrl;

}
