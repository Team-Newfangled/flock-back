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

    @Query("select t from Todo t where t.todoId.project = ?1 and t.todoId.todoDetail.teamMember.teamId.member.id = ?2")
    Page<Todo> findAllByProjectAndMemberId(
            Project todoId_project, Long memberId, Pageable pageable
    );

    void deleteAllByTodoId_Project(Project todoId_project);

}
