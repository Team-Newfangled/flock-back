package com.newfangled.flockbackend.domain.team.repository;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
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
public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {

    @Query("select distinct t from TeamMember t where t.member.id = ?1")
    List<TeamMember> findDistinctByTeamId_Member_Id(Long account_id);

    @Query("select t from TeamMember t where t.member.id = ?1 and t.team = ?2")
    Optional<TeamMember> findByTeamId_Member_IdAndTeamId_Team(
            Long teamMemberId, Team team);

    Page<TeamMember> findAllByTeamAndApproved(Team team, boolean approved, Pageable pageable);

    Optional<TeamMember> findByTeamAndMember(Team team, Member member);

    boolean existsByTeamAndMember(Team team, Member member);

}
