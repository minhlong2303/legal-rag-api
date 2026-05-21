package com.ragapi.repository;

import com.ragapi.entity.SystemAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemAccountRepository extends MongoRepository<SystemAccount, String> {

    Optional<SystemAccount> findByUsername(String username);

    Optional<SystemAccount> findByEmail(String email);
}
