package com.ragapi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Profile Entity
 * Dùng để auto-fill form pháp lý/hóa chất
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_profiles")
public class UserProfile {

    @Id
    private String id;

    // =========================
    // USER INFO
    // =========================

    @Indexed(unique = true)
    private String userId;

    private String fullName;

    private String email;

    private String phone;

    private String avatarUrl;

    // =========================
    // ORGANIZATION INFO
    // =========================

    /**
     * Tên doanh nghiệp/tổ chức
     */
    private String organizationName;

    /**
     * Địa chỉ công ty
     */
    private String organizationAddress;

    private String organizationPhone;

    private String organizationEmail;

    /**
     * Mã số thuế / ĐKKD
     */
    private String businessRegistrationNumber;

    /**
     * Giấy chứng nhận đầu tư
     */
    private String investmentCertificateNumber;

    /**
     * Mã định danh tổ chức
     */
    private String organizationId;

    /**
     * Website công ty
     */
    private String organizationWebsite;

    // =========================
    // WAREHOUSE INFO
    // =========================

    private String warehouseAddress;

    private String warehousePhone;

    // =========================
    // PRODUCTION INFO
    // =========================

    /**
     * Danh sách địa chỉ sản xuất
     */
    private List<String> productionAddresses;

    // =========================
    // LEGAL REPRESENTATIVE
    // =========================

    private String legalRepresentativeName;

    private String legalRepresentativePosition;

    private String legalRepresentativePhone;

    private String legalRepresentativeEmail;

    // =========================
    // AUTHORIZED PERSON
    // =========================

    private String authorizedPersonName;

    private String authorizedPersonPosition;

    private String authorizedPersonPhone;

    private String authorizedPersonEmail;

    // =========================
    // CUSTOMS INFO
    // =========================

    /**
     * Cửa khẩu nhập khẩu
     */
    private String customsPort;

    /**
     * Địa điểm làm thủ tục hải quan
     */
    private String customsProcedureLocation;

    // =========================
    // EXTRA DATA
    // =========================

    /**
     * Dynamic custom fields
     */
    @Builder.Default
    private Map<String, String> additionalInfo = new HashMap<>();

    // =========================
    // PROFILE STATUS
    // =========================

    /**
     * Đã hoàn thiện profile chưa
     */
    @Builder.Default
    private Boolean profileCompleted = false;

    /**
     * Đã verify email chưa
     */
    @Builder.Default
    private Boolean emailVerified = false;

    /**
     * Đã verify phone chưa
     */
    @Builder.Default
    private Boolean phoneVerified = false;

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