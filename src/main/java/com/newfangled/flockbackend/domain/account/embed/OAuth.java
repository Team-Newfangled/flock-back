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
    private String name;

}
