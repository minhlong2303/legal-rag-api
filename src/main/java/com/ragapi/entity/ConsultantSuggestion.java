package com.ragapi.entity;

import lombok.*;

import java.util.List;

/**
 * Consultant được suggest cho consultation request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantSuggestion {

    private String consultantId;

    private String consultantCode;

    private String consultantName;

    private String avatarUrl;

    private String description;

    private Double averageRating;

    private Integer completedConsultations;

    /**
     * Điểm match 0-100
     */
    private Double matchScore;

    /**
     * Lý do AI match consultant này
     */
    private String matchReason;

    private Integer responseTimeMinutes;

    private List<String> specializations;
}