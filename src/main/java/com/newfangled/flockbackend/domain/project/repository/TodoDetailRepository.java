package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoDetailRepository extends JpaRepository<TodoDetail, Long> {

    @Query("select t from TodoDetail t " +
            "where t.todo.project.id = ?1 and t.startDate between ?2 and ?3")
    List<TodoDetail> findByStartDateBetween(
            Long projectId, LocalDate startDate, LocalDate startDate2
    );

}
