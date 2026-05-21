package com.ragapi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Legal Document Entity
 * Lưu trữ văn bản pháp luật dùng cho RAG
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "legal_documents")
public class LegalDocument {

    @Id
    private String id;

    // =========================
    // BASIC INFO
    // =========================

    /**
     * Tiêu đề văn bản
     */
    @Indexed
    private String title;

    /**
     * Nội dung full text
     */
    private String content;

    /**
     * Danh mục
     * VD:
     * phap-luat
     * hoa-chat
     * moi-truong
     */
    @Indexed
    private String category;

    /**
     * Tags hỗ trợ search/filter
     */
    private List<String> tags;

    // =========================
    // SOURCE INFO
    // =========================

    /**
     * Tên file gốc
     */
    private String sourceFileName;

    /**
     * PDF / JSON / DOCX
     */
    private String sourceType;

    /**
     * GridFS file id
     */
    private String pdfFileId;

    /**
     * Kích thước file
     */
    private Long pdfFileSize;

    /**
     * MIME type
     */
    private String mimeType;

    // =========================
    // LEGAL METADATA
    // =========================

    /**
     * Số hiệu văn bản
     * VD: 32/2025/NĐ-CP
     */
    private String documentNumber;

    /**
     * Cơ quan ban hành
     */
    private String issuingAuthority;

    /**
     * Ngày ban hành
     */
    private LocalDateTime issuedDate;

    /**
     * Ngày hiệu lực
     */
    private LocalDateTime effectiveDate;

    /**
     * Trạng thái hiệu lực
     * ACTIVE / EXPIRED / DRAFT
     */
    private String legalStatus;

    // =========================
    // RAG INFO
    // =========================

    /**
     * Đã index vector chưa
     */
    @Builder.Default
    private Boolean embedded = false;

    /**
     * Tổng số chunk
     */
    private Integer chunkCount;

    // =========================
    // AUDIT
    // =========================

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}