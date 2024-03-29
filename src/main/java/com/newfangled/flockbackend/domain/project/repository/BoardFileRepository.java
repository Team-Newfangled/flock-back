package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Board;
import com.newfangled.flockbackend.domain.project.entity.sub.BoardFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoardFileRepository extends CrudRepository<BoardFile, Long> {

    @Query("select b from BoardFile b where b.board.id = ?1")
    List<BoardFile> findAllByBoard_Id(Long board_id);

    void deleteAllByBoard(Board board);

    void deleteAllByBoard_Project(Project project);

}
