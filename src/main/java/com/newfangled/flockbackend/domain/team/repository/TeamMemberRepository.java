package com.newfangled.flockbackend.domain.team.repository;

import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.global.embed.TeamId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends CrudRepository<TeamMember, TeamId> {

    @Query("select distinct t from TeamMember t where t.teamId.member.id = ?1")
    List<TeamMember> findDistinctByTeamId_Member_Id(Long account_id);

    @Query("select t from TeamMember t where t.teamId.member.id = ?1 and t.teamId.team = ?2")
    Optional<TeamMember> findByTeamId_Member_IdAndTeamId_Team(
            Long teamMemberId, Team team);

    @Query("select t from TeamMember t where t.teamId = ?1")
    Optional<TeamMember> findByTeamId(TeamId teamId);

    boolean existsByTeamId(TeamId teamId);

    Page<TeamMember> findAllByTeamId_TeamAndApproved(Team team, boolean approved, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from TeamMember t where t.teamId.team = ?1")
    void deleteAllByTeamId_Team(Team teamId_team);

}
