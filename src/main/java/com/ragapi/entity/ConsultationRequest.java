package com.ragapi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ConsultationRequest Entity - Lưu request tư vấn từ user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consultation_requests")
public class ConsultationRequest {

    @Id
    private String id;

    // =========================
    // USER INFO
    // =========================

    @Indexed
    private String userId;

    private String userName;

    private String userEmail;

    // =========================
    // QUESTION CONTEXT
    // =========================

    /**
     * Câu hỏi gốc của user
     */
    private String originalQuestion;

    /**
     * Câu trả lời từ AI/RAG
     */
    private String aiResponse;

    /**
     * Form liên quan nếu AI detect được
     * VD: ["02A", "IMPORT-CHEMICAL"]
     */
    private List<String> relatedFormCodes;

    // =========================
    // CONSULTATION STATUS
    // =========================

    /**
     * See ConsultationStatus for valid states.
     */
    @Builder.Default
    private ConsultationStatus status = ConsultationStatus.PENDING_OFFER;

    /**
     * Có nên offer consultant không
     */
    @Builder.Default
    private Boolean shouldOfferConsultation = false;

    // =========================
    // CONSULTANT INFO
    // =========================

    private String assignedConsultantId;

    private String assignedConsultantName;

    private String assignedConsultantEmail;

    // =========================
    // CHAT ROOM
    // =========================

    /**
     * Room chat sau khi user chọn consultant
     */
    private String chatRoomId;

    // =========================
    // MATCHING RESULT
    // =========================

    /**
     * Top consultant được suggest
     */
    private List<ConsultantSuggestion> suggestedConsultants;

    // =========================
    // USER FEEDBACK
    // =========================

    /**
     * Rating sau khi tư vấn xong
     */
    private Double userSatisfactionRating;

    private String userFeedback;

    /**
     * Lý do cancel nếu có
     */
    private String cancelReason;

    // =========================
    // INTERNAL
    // =========================

    private String internalNotes;

    /**
     * Thời gian consultant phản hồi
     */
    private Integer consultantResponseTimeSeconds;

    /**
     * Tổng thời gian tư vấn
     */
    private Integer durationSeconds;

    // =========================
    // TIMESTAMPS
    // =========================

    private LocalDateTime questionAskedAt;

    private LocalDateTime consultationOfferedAt;

    private LocalDateTime consultantAssignedAt;

    private LocalDateTime completedAt;

    private LocalDateTime cancelledAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
