package com.ragapi.repository;

import com.ragapi.entity.Contract;
import com.ragapi.entity.ContractStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends MongoRepository<Contract, String> {

    Optional<Contract> findByChatRoomId(String chatRoomId);

    Optional<Contract> findByConsultationRequestId(String consultationRequestId);

    List<Contract> findByUserId(String userId);

    List<Contract> findByConsultantId(String consultantId);

    List<Contract> findByStatus(ContractStatus status);
}
