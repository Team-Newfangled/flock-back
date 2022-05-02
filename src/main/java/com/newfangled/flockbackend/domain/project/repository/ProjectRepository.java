package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.embed.TeamId;
import com.newfangled.flockbackend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, TeamId> {

}
