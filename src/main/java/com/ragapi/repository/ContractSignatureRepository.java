package com.ragapi.repository;

import com.ragapi.entity.ContractSignature;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractSignatureRepository extends MongoRepository<ContractSignature, String> {

    Optional<ContractSignature> findByContractId(String contractId);

    List<ContractSignature> findByUserId(String userId);
}
