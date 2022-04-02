package com.newfangled.flockbackend.domain.account.entity;

import com.newfangled.flockbackend.domain.account.type.Provider;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
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
    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TINYBLOB")
    private byte[] picture;

}