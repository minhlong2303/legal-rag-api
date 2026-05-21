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
 * Consultant Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consultants")
public class Consultant {

    @Id
    private String id;

    // =========================
    // BASIC INFO
    // =========================

    @Indexed(unique = true)
    private String consultantCode;

    @Indexed
    private String consultantName;

    @Indexed
    private String email;

    private String phone;

    private String website;

    private String avatarUrl;

    // =========================
    // PROFILE
    // =========================

    private String companyName;

    private String description;

    private String address;

    private String city;

    private String country;

    // =========================
    // EXPERTISE
    // =========================

    private List<String> specializations;

    private List<String> categories;

    private List<String> supportedServices;
    // LEGAL_CONSULTING
    // DOCUMENT_REVIEW
    // CONTRACT_SUPPORT
    // LICENSE_APPLICATION

    private Integer experienceYears;

    private List<String> certifications;

    private List<String> languages;

    // =========================
    // CONSULTATION PRICING
    // =========================

    private Double consultationFee;

    private String currency;

    private Boolean freeFirstConsultation;

    // =========================
    // RATING & PERFORMANCE
    // =========================

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Integer totalReviews = 0;

    @Builder.Default
    private Integer completedConsultations = 0;

    @Builder.Default
    private Integer totalHoursSpent = 0;

    @Builder.Default
    private Double satisfactionRate = 0.0;

    // =========================
    // AVAILABILITY
    // =========================

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean online = false;

    private Integer responseTimeMinutes;

    private List<String> availableHours;

    @Builder.Default
    private Integer maxConcurrentChats = 5;

    @Builder.Default
    private Integer currentActiveChatSessions = 0;

    // =========================
    // SEARCH & MATCHING
    // =========================

    private List<String> keywords;

    private List<String> tags;

    @Builder.Default
    private Double aiMatchScore = 0.0;

    // =========================
    // VERIFICATION
    // =========================

    @Builder.Default
    private Boolean verified = false;

    private String verificationStatus;
    // PENDING
    // VERIFIED
    // REJECTED

    private String verificationCode;

    // =========================
    // SYSTEM
    // =========================

    private String createdBy;

    private String updatedBy;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LocalDateTime lastOnlineAt;
}
