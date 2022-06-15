package com.newfangled.flockbackend.domain.account.embed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Embeddable
public class OAuth {

    @NotNull
    private String oauthId;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "profile_img")
    private String profileImage;

    public void update(String oauthId, String email, String imageUrl) {
        this.oauthId = oauthId;
        this.email = email;
        this.profileImage = imageUrl;
    }

}
