package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import com.newfangled.flockbackend.domain.project.entity.Progress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends CrudRepository<Progress, TeamId> {

}
