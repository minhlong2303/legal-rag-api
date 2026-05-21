package com.ragapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Chat Message Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    // =========================
    // ROOM INFO
    // =========================

    @Indexed
    private String chatRoomId;

    @Indexed
    private String consultationRequestId;

    @Indexed
    private String contractId;

    // =========================
    // SENDER INFO
    // =========================

    @Indexed
    private String senderId;

    private String senderName;

    private String senderEmail;

    private String senderRole;
    // USER
    // CONSULTANT
    // ADMIN
    // SYSTEM

    private String senderAvatarUrl;

    // =========================
    // MESSAGE CONTENT
    // =========================

    private String content;

    private String messageType;
    // TEXT
    // IMAGE
    // FILE
    // SYSTEM
    // CONTRACT
    // PAYMENT

    private String attachmentUrl;

    private String attachmentName;

    private Long attachmentSize;

    private String attachmentContentType;

    // =========================
    // STATUS
    // =========================

    @Builder.Default
    private String status = "SENT";
    // SENT
    // DELIVERED
    // READ
    // DELETED

    @Builder.Default
    private Boolean edited = false;

    private String editedContent;

    private LocalDateTime editedAt;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;

    // =========================
    // READ TRACKING
    // =========================

    private List<String> readByUserIds;

    private LocalDateTime deliveredAt;

    private LocalDateTime readAt;

    // =========================
    // AI SUPPORT
    // =========================

    @Builder.Default
    private Boolean aiGenerated = false;

    private String aiSuggestion;

    private Double aiConfidenceScore;

    // =========================
    // METADATA
    // =========================

    private Integer charCount;

    @Builder.Default
    private Boolean internalNote = false;

    @Builder.Default
    private Boolean paymentRelated = false;

    @Builder.Default
    private Boolean contractRelated = false;

    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();
}