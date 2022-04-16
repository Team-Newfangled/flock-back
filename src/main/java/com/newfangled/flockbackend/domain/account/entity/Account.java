package com.newfangled.flockbackend.domain.account.entity;

import com.newfangled.flockbackend.domain.account.type.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'GOOGLE'")
    private Provider provider;

    @NotNull
    @Column(name = "picture_url")
    private String pictureUrl;

}
