package com.newfangled.flockbackend.domain.account.entity;

import com.newfangled.flockbackend.domain.account.embed.OAuth;
import com.newfangled.flockbackend.domain.account.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OAuth oAuth;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Account update(String oauthId, String email, String imageUrl) {
        this.oAuth.update(oauthId, email, imageUrl);
        return this;
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotExisted extends RuntimeException {
        public NotExisted(Long id) {
            super(String.format("'%d' is invalid account id", id));
        }
    }

}
