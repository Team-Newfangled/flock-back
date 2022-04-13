package com.newfangled.flockbackend.domain.sns.repository;

import com.newfangled.flockbackend.domain.sns.entity.Follower;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends CrudRepository<Follower, Long> {

}
