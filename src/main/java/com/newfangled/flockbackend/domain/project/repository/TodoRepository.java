package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
