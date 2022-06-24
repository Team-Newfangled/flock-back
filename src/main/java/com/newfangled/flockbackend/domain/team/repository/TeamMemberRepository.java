package com.newfangled.flockbackend.domain.team.repository;

import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {

    @Query("select t from TeamMember t where t.account.id = ?1")
    Optional<TeamMember> findByAccount_Id(Long account_id);

}
