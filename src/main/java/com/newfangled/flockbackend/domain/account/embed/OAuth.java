package com.newfangled.flockbackend.domain.account.embed;

import com.newfangled.flockbackend.domain.account.type.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Embeddable
public class OAuth {

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'GOOGLE'")
    private Provider provider;

    @NotNull
    @Column(name = "profile_img")
    private String profileImage;
}
