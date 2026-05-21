package com.ragapi.repository;

import com.ragapi.entity.LegalContractForm;
import com.ragapi.entity.LegalContractFormStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LegalContractFormRepository extends MongoRepository<LegalContractForm, String> {

    Optional<LegalContractForm> findByContractId(String contractId);

    Optional<LegalContractForm> findByChatRoomId(String chatRoomId);

    List<LegalContractForm> findByStatus(LegalContractFormStatus status);
}
