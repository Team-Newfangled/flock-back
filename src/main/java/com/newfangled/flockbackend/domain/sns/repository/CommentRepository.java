package com.newfangled.flockbackend.domain.sns.repository;

import com.newfangled.flockbackend.domain.sns.entity.SNSComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<SNSComment, Long> {

}
