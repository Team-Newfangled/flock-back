package com.newfangled.flockbackend.domain.sns.repository;

import com.newfangled.flockbackend.domain.sns.entity.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {

}
