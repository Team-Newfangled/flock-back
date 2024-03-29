package com.newfangled.flockbackend.domain.member.entity;

import com.newfangled.flockbackend.domain.member.embed.OAuth;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor @NoArgsConstructor
@DynamicUpdate
@Builder
@Entity
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NotNull
    private OAuth oAuth;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    private String company;

    public void updateCompany(String company) {
        this.company = company;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class UnauthorizedException extends BusinessException {
        public UnauthorizedException() {
            super(HttpStatus.UNAUTHORIZED, "인증 실패");
        }
    }

    public static class FailedToAuthException extends BusinessException {
        public FailedToAuthException() {
            super(HttpStatus.UNAUTHORIZED, "OAuth 인증 실패");
        }
    }

    public static class NotExistsException extends BusinessException {
        public NotExistsException() {
            super(HttpStatus.NOT_FOUND, "회원 찾지 못함");
        }
    }

}
