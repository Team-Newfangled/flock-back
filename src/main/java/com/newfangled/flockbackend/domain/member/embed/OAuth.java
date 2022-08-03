package com.newfangled.flockbackend.domain.member.embed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Embeddable
public class OAuth implements Serializable {

    @NotNull
    private String oauthId;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String pictureUrl;

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePicture(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

}
