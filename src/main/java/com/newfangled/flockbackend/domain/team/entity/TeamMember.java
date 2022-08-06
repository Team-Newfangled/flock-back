package com.newfangled.flockbackend.domain.team.entity;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.embed.TeamId;
import com.newfangled.flockbackend.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "team_member")
public class TeamMember {

    @EmbeddedId
    private TeamId teamId;

    @OneToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static class NoMemberException extends BusinessException {
        public NoMemberException() {
            super(HttpStatus.NOT_FOUND, "팀원을 찾지 못했습니다.");
        }
    }

    public static class NoPermissionException extends BusinessException {
        public NoPermissionException() {
            super(HttpStatus.FORBIDDEN, "퇴출시킬 수 있는 권한이 없습니다.");
        }
    }

}
