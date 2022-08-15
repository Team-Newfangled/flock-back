package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.entity.sub.BoardFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFileRepository extends CrudRepository<BoardFile, Long> {

    @Query("select b from BoardFile b where b.board.id = ?1")
    List<BoardFile> findAllByBoard_Id(Long board_id);

}
