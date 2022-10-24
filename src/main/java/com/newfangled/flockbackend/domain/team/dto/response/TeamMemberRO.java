package com.newfangled.flockbackend.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamMemberRO {

    private long id;
    private String name;
    private String role;
    private boolean approved;

    @JsonProperty("self-url")
    private String selfUrl;

    public TeamMemberRO(TeamMember teamMember) {
        Member member = teamMember.getMember();
        this.id = member.getId();
        this.name = member.getOAuth().getName();
        this.role = teamMember.getRole().name();
        this.approved = teamMember.isApproved();
        this.selfUrl = String.format("users/%d", id);
    }
}
