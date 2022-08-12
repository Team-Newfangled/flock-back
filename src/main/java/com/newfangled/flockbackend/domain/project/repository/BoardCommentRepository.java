package com.newfangled.flockbackend.domain.project.repository;

import com.newfangled.flockbackend.domain.project.entity.sub.Board;
import com.newfangled.flockbackend.domain.project.entity.sub.BoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    Page<BoardComment> findAllByBoard(Board board, Pageable pageable);

}
