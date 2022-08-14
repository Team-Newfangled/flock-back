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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "approved")
    private boolean approved;

    public void setApproved() {
        this.approved = true;
    }

    public static class NoMemberException extends BusinessException {
        public NoMemberException() {
            super(HttpStatus.NOT_FOUND, "팀원을 찾지 못했습니다.");
        }
    }

    public static class NoPermissionException extends BusinessException {
        public NoPermissionException() {
            super(HttpStatus.FORBIDDEN, "접근할 수 있는 권한이 없습니다.");
        }
    }

    public static class AlreadyApprovedException extends BusinessException {
        public AlreadyApprovedException() {
            super(HttpStatus.CONFLICT, "이미 승인된 회원입니다.");
        }
    }
}
