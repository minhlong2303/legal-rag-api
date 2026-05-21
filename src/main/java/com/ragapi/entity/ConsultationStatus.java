package com.ragapi.entity;

/**
 * Status values for a consultation request.
 */
public enum ConsultationStatus {

    PENDING_AI,
    WAITING_CONSULTANT,
    PENDING_OFFER,
    CONSULTANT_OFFERED,
    OFFERED,
    ACCEPTED,
    CONSULTANT_SELECTED,
    IN_CHAT,
    CONTRACT_CREATED,
    PAYMENT_PENDING,
    PAID,
    COMPLETED,
    CANCELLED
}
