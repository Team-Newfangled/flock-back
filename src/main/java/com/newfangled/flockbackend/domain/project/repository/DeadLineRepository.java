package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import com.newfangled.flockbackend.domain.project.entity.DeadLine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadLineRepository extends CrudRepository<DeadLine, TeamId> {

}
