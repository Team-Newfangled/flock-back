package com.newfangled.flockbackend.domain.team.repository;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {

}
