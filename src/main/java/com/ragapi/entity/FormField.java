package com.ragapi.entity;

import lombok.*;

import java.util.List;

/**
 * Form Field Entity - Đại diện cho một field trong form
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormField {

    /**
     * ID unique của field
     * VD: organization_name
     */
    private String fieldId;

    /**
     * Tên field để bind dữ liệu
     * VD: organizationName
     */
    private String fieldName;

    /**
     * Label hiển thị ngoài UI
     * VD: "Tên tổ chức"
     */
    private String displayLabel;

    /**
     * TEXT
     * TEXTAREA
     * NUMBER
     * DATE
     * SELECT
     * TABLE
     * EMAIL
     * PHONE
     * FILE
     */
    private String fieldType;

    /**
     * Placeholder hiển thị
     */
    private String placeholder;

    /**
     * Có bắt buộc không
     */
    @Builder.Default
    private Boolean required = false;

    /**
     * Rule validate
     * VD:
     * required
     * email
     * phone
     * max:255
     */
    private List<String> validationRules;

    /**
     * Gợi ý giá trị
     */
    private List<String> suggestions;

    /**
     * Nguồn auto fill
     * VD:
     * user.companyName
     * user.phone
     * static.today
     */
    private String autoFillSource;

    /**
     * Giá trị mặc định
     */
    private Object defaultValue;

    /**
     * Field có read-only không
     */
    @Builder.Default
    private Boolean readOnly = false;

    /**
     * Field có hidden không
     */
    @Builder.Default
    private Boolean hidden = false;

    /**
     * Thứ tự hiển thị trên UI
     */
    private Integer displayOrder;

    /**
     * Ghi chú thêm
     */
    private String notes;
}