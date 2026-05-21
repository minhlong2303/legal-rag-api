package com.ragapi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Form Template Entity
 * Đại diện cho một biểu mẫu pháp lý/hóa chất
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "form_templates")
public class FormTemplate {

    @Id
    private String id;

    // =========================
    // FORM METADATA
    // =========================

    /**
     * Mã form
     * VD: 02A
     */
    @Indexed(unique = true)
    private String formCode;

    /**
     * Tên form
     */
    private String formName;

    /**
     * IMPORT_CHEMICAL_PERMIT
     * EXPORT_CHEMICAL_PERMIT
     * REISSUE_PERMIT
     */
    private String formType;

    /**
     * Mô tả form
     */
    private String description;

    // =========================
    // SEARCH & MATCHING
    // =========================

    /**
     * Keywords để AI match
     */
    private List<String> keywords;

    /**
     * Category để filter/search
     */
    private List<String> categories;

    /**
     * Điểm ưu tiên khi suggest
     */
    @Builder.Default
    private Integer priority = 0;

    // =========================
    // FORM STRUCTURE
    // =========================

    /**
     * Danh sách field
     */
    private List<FormField> fields;

    /**
     * Markdown template gốc
     */
    private String placeholderMarkdown;

    /**
     * HTML render sẵn cho frontend
     */
    private String htmlTemplate;

    // =========================
    // FILE INFO
    // =========================

    /**
     * Tên file markdown gốc
     */
    private String sourceFileName;

    /**
     * Version form
     */
    @Builder.Default
    private Integer version = 1;

    // =========================
    // STATUS
    // =========================

    @Builder.Default
    private Boolean active = true;

    /**
     * Form deprecated hay chưa
     */
    @Builder.Default
    private Boolean deprecated = false;

    // =========================
    // AUDIT
    // =========================

    private String createdBy;

    private String updatedBy;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}