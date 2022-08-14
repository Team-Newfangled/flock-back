package com.newfangled.flockbackend.domain.member.repository;

import com.newfangled.flockbackend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select a from Member a where a.oAuth.oauthId = ?1")
    Optional<Member> findByOAuth_OauthId(String OAuth_oauthId);

    @Query("select m from Member m where m.oAuth.email = ?1")
    Optional<Member> findByOAuth_Email(@NotNull String OAuth_email);

}
