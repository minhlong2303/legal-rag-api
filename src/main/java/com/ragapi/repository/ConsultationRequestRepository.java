package com.ragapi.repository;

import com.ragapi.entity.ConsultationRequest;
import com.ragapi.entity.ConsultationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRequestRepository extends MongoRepository<ConsultationRequest, String> {
    
    /**
     * Tìm request theo userId
     */
    List<ConsultationRequest> findByUserId(String userId);
    
    /**
     * Tìm request theo consultantId
     */
    List<ConsultationRequest> findByAssignedConsultantId(String consultantId);
    
    /**
     * Tìm request theo status
     */
    List<ConsultationRequest> findByStatus(ConsultationStatus status);
    
    /**
     * Tìm request theo userId và status
     */
    List<ConsultationRequest> findByUserIdAndStatus(String userId, ConsultationStatus status);
    
    /**
     * Tìm request pending offer (status = PENDING_OFFER)
     */
    List<ConsultationRequest> findByStatusAndConsultationOfferedAtIsNull(ConsultationStatus status);
    
    /**
     * Tìm request theo chatRoomId
     */
    Optional<ConsultationRequest> findByChatRoomId(String chatRoomId);
}

