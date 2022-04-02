package com.newfangled.flockbackend.domain.team.repository;

import com.newfangled.flockbackend.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
