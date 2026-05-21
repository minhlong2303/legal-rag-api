package com.ragapi.service;

import com.ragapi.dto.*;
import com.ragapi.entity.ChatRoom;
import com.ragapi.entity.Consultant;
import com.ragapi.entity.ConsultationRequest;
import com.ragapi.entity.ConsultationStatus;
import com.ragapi.repository.ChatRoomRepository;
import com.ragapi.repository.ConsultantRepository;
import com.ragapi.repository.ConsultationRequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ConsultationService - Quản lý consultation requests
 */
@Slf4j
@Service
@AllArgsConstructor
public class ConsultationService {
    
    private ConsultationRequestRepository consultationRequestRepository;
    private ConsultantRepository consultantRepository;
    private ChatRoomRepository chatRoomRepository;
    private ConsultantMatchingService matchingService;
    
    /**
     * Tạo consultation request khi user hỏi câu hỏi
     */
    public ConsultationRequest createConsultationRequest(
            String userId, 
            String userEmail, 
            String userName,
            String question, 
            String aiResponse) {
        
        ConsultationRequest request = ConsultationRequest.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .userEmail(userEmail)
                .userName(userName)
                .originalQuestion(question)
                .aiResponse(aiResponse)
                .status(ConsultationStatus.PENDING_OFFER)
                .questionAskedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return consultationRequestRepository.save(request);
    }
    
    /**
     * Offer consultation sau 30 giây
     * (Được gọi từ scheduler task)
     */
    public ConsultationOfferResponse offerConsultation(String consultationRequestId) {
        Optional<ConsultationRequest> opt = consultationRequestRepository.findById(consultationRequestId);
        
        if (opt.isEmpty()) {
            log.error("Consultation request not found: {}", consultationRequestId);
            return null;
        }
        
        ConsultationRequest request = opt.get();
        
        // Gọi service matching để tìm consultant phù hợp
        List<ConsultantSuggestionDTO> suggestions = matchingService
                .findMatchingConsultants(request.getOriginalQuestion(), 5);
        
        // Update request status
        request.setStatus(ConsultationStatus.OFFERED);
        request.setConsultationOfferedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        consultationRequestRepository.save(request);
        
        log.info("Consultation offered for request {}: found {} consultants", consultationRequestId, suggestions.size());
        
        return ConsultationOfferResponse.builder()
                .consultationRequestId(consultationRequestId)
                .shouldOfferConsultation(true)
                .suggestedConsultants(suggestions)
                .message("Bạn có muốn được tư vấn thêm từ các chuyên gia hàng đầu không?")
                .build();
    }
    
    /**
     * User lựa chọn consultant và tạo chat room
     */
    public ConsultantSelectionResponse selectConsultant(
            String consultationRequestId,
            String userId,
            String selectedConsultantId) {
        
        // 1. Get consultation request
        Optional<ConsultationRequest> requestOpt = consultationRequestRepository.findById(consultationRequestId);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Consultation request không tìm thấy");
        }
        
        // 2. Get consultant
        Optional<Consultant> consultantOpt = consultantRepository.findById(selectedConsultantId);
        if (consultantOpt.isEmpty()) {
            throw new RuntimeException("Consultant không tìm thấy");
        }
        
        ConsultationRequest request = requestOpt.get();
        Consultant consultant = consultantOpt.get();
        
        // 3. Create chat room
        ChatRoom chatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .consultantId(consultant.getId())
                .consultantName(consultant.getConsultantName())
                .consultantEmail(consultant.getEmail())
                .consultationRequestId(consultationRequestId)
                .originalQuestion(request.getOriginalQuestion())
                .aiResponse(request.getAiResponse())
                .status("ACTIVE")
                .messageCount(0)
                .userMessageCount(0)
                .consultantMessageCount(0)
                .createdAt(LocalDateTime.now())
                .topic(extractTopic(request.getOriginalQuestion()))
                .build();
        
        chatRoom = chatRoomRepository.save(chatRoom);
        
        // 4. Update consultation request
        request.setStatus(ConsultationStatus.IN_CHAT);
        request.setAssignedConsultantId(consultant.getId());
        request.setAssignedConsultantName(consultant.getConsultantName());
        request.setAssignedConsultantEmail(consultant.getEmail());
        request.setChatRoomId(chatRoom.getId());
        request.setConsultantAssignedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        consultationRequestRepository.save(request);
        
        // 5. Increment consultant active chat count
        consultant.setCurrentActiveChatSessions(
                (consultant.getCurrentActiveChatSessions() != null ? consultant.getCurrentActiveChatSessions() : 0) + 1
        );
        consultant.setUpdatedAt(LocalDateTime.now());
        consultantRepository.save(consultant);
        
        log.info("Chat room created: {} between user {} and consultant {}", 
                chatRoom.getId(), userId, selectedConsultantId);
        
        return ConsultantSelectionResponse.builder()
                .chatRoomId(chatRoom.getId())
                .consultantName(consultant.getConsultantName())
                .consultantEmail(consultant.getEmail())
                .message("Bạn sẽ được tư vấn bởi " + consultant.getConsultantName())
                .build();
    }
    
    /**
     * User cancel consultation offer
     */
    public void cancelConsultationOffer(String consultationRequestId, String userId, String reason) {
        Optional<ConsultationRequest> opt = consultationRequestRepository.findById(consultationRequestId);
        
        if (opt.isPresent()) {
            ConsultationRequest request = opt.get();
            request.setStatus(ConsultationStatus.CANCELLED);
            request.setCancelReason(reason);
            request.setUpdatedAt(LocalDateTime.now());
            consultationRequestRepository.save(request);
            
            log.info("Consultation cancelled: {} - Reason: {}", consultationRequestId, reason);
        }
    }
    
    /**
     * Get consultation history cho user
     */
    public List<ConsultationRequest> getUserConsultationHistory(String userId) {
        return consultationRequestRepository.findByUserId(userId);
    }

    public ConsultationRequest getConsultationDetail(String consultationRequestId) {
        return consultationRequestRepository.findById(consultationRequestId)
                .orElseThrow(() -> new RuntimeException("Consultation request not found"));
    }
    
    /**
     * Get pending consultation requests (chưa được offer)
     */
    public List<ConsultationRequest> getPendingConsultationRequests() {
        return consultationRequestRepository.findByStatus(ConsultationStatus.PENDING_OFFER);
    }
    
    /**
     * Extract topic từ question
     */
    private String extractTopic(String question) {
        if (question == null || question.isEmpty()) {
            return "Tư vấn chung";
        }
        
        if (question.contains("nhập")) return "Nhập khẩu hóa chất";
        if (question.contains("xuất")) return "Xuất khẩu hóa chất";
        if (question.contains("sản")) return "Sản xuất hóa chất";
        if (question.contains("lưu trữ")) return "Lưu trữ hóa chất";
        if (question.contains("giấy phép")) return "Cấp phép hóa chất";
        
        return "Tư vấn hóa chất";
    }
}

