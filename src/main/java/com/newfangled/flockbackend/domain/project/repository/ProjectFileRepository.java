package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import com.newfangled.flockbackend.domain.project.entity.ProjectFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFileRepository extends CrudRepository<ProjectFile, TeamId> {

}
