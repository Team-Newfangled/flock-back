package com.newfangled.flockbackend.domain.team.repository;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.global.embed.TeamId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends CrudRepository<TeamMember, TeamId> {

    @Query("select t from TeamMember t where t.member.id = ?1")
    Optional<TeamMember> findByMember_Id(Long account_id);

    @Query("select distinct t from TeamMember t where t.member.id = ?1")
    List<TeamMember> findDistinctByMember_Id(Long account_id);

    Optional<TeamMember> findByMember_IdAndTeamId(Long member_id, TeamId teamId);

    Page<TeamMember> findAllByTeamIdAndApproved(TeamId teamId, boolean approved, Pageable pageable);

}
