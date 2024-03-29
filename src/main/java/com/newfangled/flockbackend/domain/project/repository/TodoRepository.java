package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select t from Todo t where t.project = ?1 and t.todoDetail.teamMember.member.id = ?2")
    Page<Todo> findAllByProjectAndMemberId(
            Project todoId_project, Long memberId, Pageable pageable
    );

    void deleteAllByProject(Project todoId_project);

    Page<Todo> findAllByProject(Project project, Pageable pageable);

}
