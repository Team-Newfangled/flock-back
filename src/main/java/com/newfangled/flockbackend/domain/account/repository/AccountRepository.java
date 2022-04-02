package com.newfangled.flockbackend.domain.account.repository;

import com.newfangled.flockbackend.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
