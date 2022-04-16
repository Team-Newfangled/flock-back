package com.newfangled.flockbackend.domain.sns.repository;

import com.newfangled.flockbackend.domain.sns.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

}
