package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.global.embed.TeamId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByTeamId(TeamId teamId, Pageable pageable);

}
