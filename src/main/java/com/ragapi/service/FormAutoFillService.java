package com.ragapi.service;

import com.ragapi.dto.*;
import com.ragapi.entity.FormTemplate;
import com.ragapi.entity.UserProfile;
import com.ragapi.entity.FormField;
import com.ragapi.repository.FormTemplateRepository;
import com.ragapi.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service để auto-fill form từ user profile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FormAutoFillService {
    
    private final FormTemplateRepository formRepository;
    private final UserProfileRepository userProfileRepository;
    
    /**
     * Get form detail với auto-fill data
     */
    public FormDetailResponse getFormDetailWithAutoFill(String formId, String userId) {
        log.info("Getting form detail with auto-fill: formId={}, userId={}", formId, userId);
        
        // Lấy form template
        FormTemplate form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found: " + formId));
        
        // Lấy user profile (nếu userId có)
        UserProfile userProfile = null;
        if (userId != null) {
            userProfile = userProfileRepository.findByUserId(userId).orElse(null);
        }
        
        // Chuẩn bị response
        FormDetailResponse response = new FormDetailResponse();
        
        // Form metadata
        FormMetadata metadata = new FormMetadata();
        metadata.setId(form.getId());
        metadata.setFormCode(form.getFormCode());
        metadata.setFormName(form.getFormName());
        metadata.setDescription(form.getDescription());
        metadata.setFormType(form.getFormType());
        response.setForm(metadata);
        
        // Auto-fill data dựa trên user profile
        Map<String, Object> filledData = new HashMap<>();
        Map<String, FormFieldInfo> emptyFields = new HashMap<>();
        
        if (form.getFields() != null) {
            for (FormField field : form.getFields()) {
                String autoFillValue = getAutoFillValue(field, userProfile);
                
                if (autoFillValue != null && !autoFillValue.isEmpty()) {
                    // Có data để fill
                    filledData.put(field.getFieldId(), autoFillValue);
                } else {
                    // Field trống
                    FormFieldInfo fieldInfo = new FormFieldInfo();
                    fieldInfo.setFieldId(field.getFieldId());
                    fieldInfo.setDisplayLabel(field.getDisplayLabel());
                    fieldInfo.setFieldType(field.getFieldType());
                    fieldInfo.setPlaceholder(field.getPlaceholder());
                    fieldInfo.setRequired(field.getRequired());
                    fieldInfo.setSuggestions(field.getSuggestions());
                    fieldInfo.setNotes(field.getNotes());
                    
                    emptyFields.put(field.getFieldId(), fieldInfo);
                }
            }
        }
        
        response.setFilledData(filledData);
        response.setEmptyFields(emptyFields);
        response.setFormTemplate(generateFormTemplate(form, filledData));
        
        return response;
    }
    
    /**
     * Get auto-fill value từ user profile
     */
    private String getAutoFillValue(FormField field, UserProfile userProfile) {
        if (userProfile == null || field.getAutoFillSource() == null) {
            return null;
        }
        
        String source = field.getAutoFillSource();
        
        // Source format: "user.fieldName" hoặc "static"
        if (source.startsWith("user.")) {
            String profileFieldName = source.substring("user.".length());
            return getProfileFieldValue(userProfile, profileFieldName);
        }
        
        return null;
    }
    
    /**
     * Get value từ user profile dựa trên field name
     */
    private String getProfileFieldValue(UserProfile profile, String fieldName) {
        try {
            // Sử dụng reflection để lấy giá trị
            Field field = UserProfile.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(profile);
            
            return value != null ? value.toString() : null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("Field not found in UserProfile: {}", fieldName);
            return null;
        }
    }
    
    /**
     * Generate HTML template từ form
     */
    private String generateFormTemplate(FormTemplate form, Map<String, Object> filledData) {
        StringBuilder html = new StringBuilder();
        
        html.append("<form class=\"chemical-form\">\n");
        html.append("<h2>").append(form.getFormName()).append("</h2>\n");
        
        if (form.getFields() != null) {
            for (FormField field : form.getFields()) {
                html.append(generateFieldHTML(field, filledData.get(field.getFieldId())));
            }
        }
        
        html.append("<button type=\"submit\" class=\"btn-submit\">Gửi Form</button>\n");
        html.append("</form>\n");
        
        return html.toString();
    }
    
    /**
     * Generate HTML cho một field
     */
    private String generateFieldHTML(FormField field, Object filledValue) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"form-group").append(field.getRequired() ? " required" : "").append("\">\n");
        html.append("<label for=\"").append(field.getFieldId()).append("\">")
                .append(field.getDisplayLabel()).append("</label>\n");
        
        String value = filledValue != null ? filledValue.toString() : "";
        
        switch (field.getFieldType()) {
            case "TEXT":
                html.append("<input type=\"text\" id=\"").append(field.getFieldId())
                        .append("\" name=\"").append(field.getFieldId())
                        .append("\" placeholder=\"").append(field.getPlaceholder() != null ? field.getPlaceholder() : "")
                        .append("\" value=\"").append(value).append("\"");
                if (field.getRequired()) html.append(" required");
                html.append(">\n");
                break;
                
            case "TEXTAREA":
                html.append("<textarea id=\"").append(field.getFieldId())
                        .append("\" name=\"").append(field.getFieldId())
                        .append("\" placeholder=\"").append(field.getPlaceholder() != null ? field.getPlaceholder() : "")
                        .append("\"");
                if (field.getRequired()) html.append(" required");
                html.append(">").append(value).append("</textarea>\n");
                break;
                
            case "NUMBER":
                html.append("<input type=\"number\" id=\"").append(field.getFieldId())
                        .append("\" name=\"").append(field.getFieldId())
                        .append("\" value=\"").append(value).append("\"");
                if (field.getRequired()) html.append(" required");
                html.append(">\n");
                break;
                
            case "DATE":
                html.append("<input type=\"date\" id=\"").append(field.getFieldId())
                        .append("\" name=\"").append(field.getFieldId())
                        .append("\" value=\"").append(value).append("\"");
                if (field.getRequired()) html.append(" required");
                html.append(">\n");
                break;
                
            case "SELECT":
                html.append("<select id=\"").append(field.getFieldId())
                        .append("\" name=\"").append(field.getFieldId()).append("\"");
                if (field.getRequired()) html.append(" required");
                html.append(">\n");
                html.append("<option value=\"\">-- Chọn --</option>\n");
                
                if (field.getSuggestions() != null) {
                    for (String suggestion : field.getSuggestions()) {
                        String selected = suggestion.equals(value) ? " selected" : "";
                        html.append("<option value=\"").append(suggestion).append("\"").append(selected)
                                .append(">").append(suggestion).append("</option>\n");
                    }
                }
                html.append("</select>\n");
                break;
        }
        
        if (field.getNotes() != null && !field.getNotes().isEmpty()) {
            html.append("<small class=\"field-note\">").append(field.getNotes()).append("</small>\n");
        }
        
        html.append("</div>\n");
        
        return html.toString();
    }
    
    /**
     * Update user profile từ form data
     */
    public UserProfile saveUserProfileFromForm(String userId, Map<String, Object> formData) {
        log.info("Saving user profile from form data: userId={}", userId);
        
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElse(new UserProfile());
        
        profile.setUserId(userId);
        
        // Map form data sang profile fields
        formData.forEach((key, value) -> {
            if (value != null) {
                try {
                    Field field = UserProfile.class.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(profile, value.toString());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.warn("Cannot set field {}: {}", key, e.getMessage());
                }
            }
        });
        
        profile.setProfileCompleted(true);
        profile.setUpdatedAt(LocalDateTime.now());
        
        return userProfileRepository.save(profile);
    }

    public FormSubmitResponse submitForm(FormSubmitRequest request) {
        saveUserProfileFromForm(request.getUserId(), request.getFormData());

        return FormSubmitResponse.builder()
                .status(Boolean.TRUE.equals(request.getDraft()) ? "DRAFT_SAVED" : "SUCCESS")
                .message(Boolean.TRUE.equals(request.getDraft()) ? "Draft saved" : "Form submitted")
                .submittedFormId(UUID.randomUUID().toString())
                .sentToConsultant(request.getSendToConsultant())
                .consultantId(request.getConsultantId())
                .paymentRequired(false)
                .submittedAt(LocalDateTime.now())
                .build();
    }

    public UserProfile updateUserProfile(String userId, Map<String, Object> profileData) {
        return saveUserProfileFromForm(userId, profileData);
    }
}




