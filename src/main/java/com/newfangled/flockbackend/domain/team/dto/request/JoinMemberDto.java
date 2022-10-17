package com.newfangled.flockbackend.domain.team.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class JoinMemberDto {

    @JsonProperty("member_id")
    private long memberId;
}
