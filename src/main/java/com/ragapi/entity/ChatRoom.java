package com.ragapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Chat Room Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom {

    @Id
    private String id;

    // =========================
    // USER INFO
    // =========================

    @Indexed
    private String userId;

    private String userName;

    private String userEmail;

    private String userAvatarUrl;

    // =========================
    // CONSULTANT INFO
    // =========================

    @Indexed
    private String consultantId;

    private String consultantName;

    private String consultantEmail;

    private String consultantAvatarUrl;

    // =========================
    // RELATED DATA
    // =========================

    @Indexed
    private String consultationRequestId;

    @Indexed
    private String contractId;

    @Indexed
    private String paymentConfirmationId;

    // =========================
    // CONTEXT
    // =========================

    private String topic;

    private String category;

    private String originalQuestion;

    private String aiResponse;

    // =========================
    // ROOM STATUS
    // =========================

    @Builder.Default
    private String status = "ACTIVE";
    // ACTIVE
    // WAITING_PAYMENT
    // CONTRACT_PENDING
    // CONTRACT_SIGNED
    // CLOSED
    // CANCELLED

    @Builder.Default
    private Boolean unread = false;

    @Builder.Default
    private Boolean consultantJoined = false;

    @Builder.Default
    private Boolean contractCreated = false;

    @Builder.Default
    private Boolean paymentConfirmed = false;

    // =========================
    // LAST MESSAGE
    // =========================

    private String lastMessage;

    private String lastSenderId;

    private LocalDateTime lastMessageAt;

    // =========================
    // STATISTICS
    // =========================

    @Builder.Default
    private Integer messageCount = 0;

    @Builder.Default
    private Integer unreadUserCount = 0;

    @Builder.Default
    private Integer unreadConsultantCount = 0;

    @Builder.Default
    private Integer userMessageCount = 0;

    @Builder.Default
    private Integer consultantMessageCount = 0;

    // =========================
    // FEEDBACK
    // =========================

    private Double finalRating;

    private String finalFeedback;

    // =========================
    // TIMESTAMPS
    // =========================

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime consultantJoinedAt;

    private LocalDateTime contractCreatedAt;

    private LocalDateTime paymentConfirmedAt;

    private LocalDateTime closedAt;
}