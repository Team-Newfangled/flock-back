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

    @Query("select t from Todo t where t.todoId.project = ?1 and t.todoId.todoDetail.teamMember.member.id = ?2")
    Page<Todo> findAllByTodoId_ProjectAndTodoId_TodoDetail_TeamMember_Member_Id(Project todoId_project, Long todoId_todoDetail_teamMember_member_id, Pageable pageable);

}
