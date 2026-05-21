package com.ragapi.service;

import com.ragapi.entity.FormTemplate;
import com.ragapi.repository.FormTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import com.ragapi.entity.FormField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service để parse file .md một cách giới giải
 * Extract forms từ file .md định dạng này (Phụ Lục II, III của Thông tư 01/2026)
 * 
 * File structure:
 * - Phụ Lục II: Biểu mẫu nhập khẩu hóa chất cấm (Mẫu 02a, 02b, 02c)
 * - Phụ Lục III: Biểu mẫu sản xuất hóa chất cấm (Mẫu 03a, 03b, 03c)
 * - Phụ Lục IV-VI: Các biểu mẫu khác
 * 
 * Mỗi mẫu có format:
 * **Mẫu XXx. [Name]**
 * [Content]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FormTemplateParserService {
    
    private final FormTemplateRepository formRepository;
    
    /**
     * Parse file .md content và populate forms vào database
     */
    public void parseAndLoadForms(String mdContent) {
        log.info("Starting to parse form templates from .md file...");
        
        // Extract tất cả forms
        List<FormTemplate> forms = extractFormsFromMarkdown(mdContent);
        
        // Save vào database
        for (FormTemplate form : forms) {
            try {
                formRepository.save(form);
                log.info("Saved form: {} ({})", form.getFormCode(), form.getFormName());
            } catch (Exception e) {
                log.error("Error saving form: {}", form.getFormCode(), e);
            }
        }
        
        log.info("Finished loading {} form templates", forms.size());
    }

    public void loadTemplatesFromResources() {
        loadHardcodedForms();
    }
    
    /**
     * Extract forms từ markdown content
     */
    private List<FormTemplate> extractFormsFromMarkdown(String mdContent) {
        List<FormTemplate> forms = new ArrayList<>();
        
        // Pattern để tìm mỗi form: **Mẫu XXx. [Name]**
        Pattern formPattern = Pattern.compile("\\*\\*Mẫu\\s+([0-9a-zA-Z]+)\\.\\s+([^\\*]+)\\*\\*");
        Matcher formMatcher = formPattern.matcher(mdContent);
        
        while (formMatcher.find()) {
            String formCode = formMatcher.group(1).trim();
            String formName = formMatcher.group(2).trim();
            
            log.info("Found form: {} - {}", formCode, formName);
            
            // Create form template
            FormTemplate form = createFormTemplate(formCode, formName, mdContent);
            if (form != null) {
                forms.add(form);
            }
        }
        
        return forms;
    }
    
    /**
     * Create FormTemplate object từ form code và name
     */
    private FormTemplate createFormTemplate(String formCode, String formName, String mdContent) {
        FormTemplate form = new FormTemplate();
        form.setFormCode(formCode);
        form.setFormName(formName);
        
        // Determine form type based on form code
        form.setFormType(determineFormType(formCode, formName));
        
        // Set description
        form.setDescription(generateDescription(formCode, formName));
        
        // Extract keywords từ form name
        form.setKeywords(extractKeywords(formName));
        
        // Set categories
        form.setCategories(determineCategories(formCode, formName));
        
        // Extract fields - đây là phần phức tạp
        // Tạm thời để trống, sau tạo hàm riêng để extract fields
        form.setFields(extractFormFields(formCode, formName, mdContent));
        
        // Set active
        form.setActive(true);
        form.setCreatedAt(java.time.LocalDateTime.now());
        form.setUpdatedAt(java.time.LocalDateTime.now());
        
        return form;
    }
    
    /**
     * Determine form type based on form code
     */
    private String determineFormType(String formCode, String formName) {
        // Mẫu 02a, 02b, 02c: Nhập khẩu hóa chất cấm
        if (formCode.startsWith("02")) {
            return "IMPORT_CHEMICAL_PERMIT";
        }
        // Mẫu 03a, 03b, 03c: Sản xuất hóa chất cấm
        else if (formCode.startsWith("03")) {
            return "PRODUCTION_CHEMICAL_PERMIT";
        }
        // Mẫu 04x: Xuất khẩu hóa chất cấm
        else if (formCode.startsWith("04")) {
            return "EXPORT_CHEMICAL_PERMIT";
        }
        // Mẫu 05x: Transshipment
        else if (formCode.startsWith("05")) {
            return "TRANSSHIPMENT_PERMIT";
        }
        
        return "OTHER_PERMIT";
    }
    
    /**
     * Generate description từ form name
     */
    private String generateDescription(String formCode, String formName) {
        return formName + " (Mẫu " + formCode + ")";
    }
    
    /**
     * Extract keywords từ form name
     */
    private List<String> extractKeywords(String formName) {
        List<String> keywords = new ArrayList<>();
        
        // Keywords tiếng Việt thường gặp
        String[] commonKeywords = {
            "cấp", "giấy phép", "hóa chất", "cấm", "nhập", "khẩu", "sản xuất",
            "xuất", "đề nghị", "điều chỉnh", "cấp lại", "văn bản", "nguy hại",
            "import", "export", "chemical", "permit", "banned", "production"
        };
        
        String normalizedName = normalizeText(formName);
        for (String keyword : commonKeywords) {
            if (normalizedName.contains(normalizeText(keyword))) {
                keywords.add(keyword);
            }
        }
        
        return keywords;
    }
    
    /**
     * Determine categories based on form code
     */
    private List<String> determineCategories(String formCode, String formName) {
        List<String> categories = new ArrayList<>();
        
        // Add primary category based on form code
        if (formCode.startsWith("02")) {
            categories.add("import");
        } else if (formCode.startsWith("03")) {
            categories.add("production");
        } else if (formCode.startsWith("04")) {
            categories.add("export");
        } else if (formCode.startsWith("05")) {
            categories.add("transshipment");
        }
        
        // All chemical related
        categories.add("chemical");
        categories.add("permit");
        
        // Add sub-category based on whether it's application (a), reissue (b), or template (c)
        if (formCode.endsWith("a")) {
            categories.add("application");
        } else if (formCode.endsWith("b")) {
            categories.add("reissue");
        } else if (formCode.endsWith("c")) {
            categories.add("template");
        }
        
        return categories;
    }
    
    /**
     * Extract form fields từ markdown content
     * NOTE: Đây là phần khó nhất - cần parse HTML tables trong markdown
     */
    private List<FormField> extractFormFields(String formCode, String formName, String mdContent) {
        List<FormField> fields = new ArrayList<>();
        
        // Tạm thời return empty list
        // TODO: Implement field extraction logic
        // - Tìm bảng HTML sau form section
        // - Extract các rows
        // - Map vào FormField objects
        
        // Các field thường gặp dựa trên form code:
        if (formCode.startsWith("02") || formCode.startsWith("03")) {
            // Mẫu 02a, 02b, 03a, 03b: Các form đề nghị
            // Common fields: tên tổ chức, địa chỉ, điện thoại, giấy đăng ký, v.v.
        } else if (formCode.startsWith("02c") || formCode.startsWith("03c")) {
            // Mẫu 02c, 03c: Biểu mẫu giấy phép
            // Different structure
        }
        
        return fields;
    }
    
    /**
     * Normalize text (lowercase, remove diacritics)
     */
    private String normalizeText(String text) {
        if (text == null) return "";
        
        return text.toLowerCase()
                .replaceAll("[àáảãạăằắẳẵặâầấẩẫậ]", "a")
                .replaceAll("[èéẻẽẹêềếểễệ]", "e")
                .replaceAll("[ìíỉĩị]", "i")
                .replaceAll("[òóỏõọôồốổỗộơờớởỡợ]", "o")
                .replaceAll("[ùúủũụưừứửữự]", "u")
                .replaceAll("[ỳýỷỹỵ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s]", "");
    }
    
    /**
     * Thêm cách khác: Load forms hard-coded
     * Nếu parsing markdown quá phức tạp, có thể hard-code các forms
     */
    public void loadHardcodedForms() {
        log.info("Loading hard-coded forms...");
        
        // Mẫu 02a: Văn bản đề nghị cấp Giấy phép nhập khẩu hóa chất cấm
        FormTemplate form02a = new FormTemplate();
        form02a.setFormCode("02a");
        form02a.setFormName("Văn bản đề nghị cấp Giấy phép nhập khẩu hóa chất cấm");
        form02a.setFormType("IMPORT_CHEMICAL_PERMIT");
        form02a.setDescription("Biểu mẫu đề nghị cấp giấy phép nhập khẩu hóa chất cấm theo Thông tư 01/2026/TT-BCT");
        form02a.setKeywords(Arrays.asList("nhập khẩu", "hóa chất cấm", "cấp phép", "import", "chemical", "banned"));
        form02a.setCategories(Arrays.asList("import", "chemical", "permit", "application"));
        form02a.setActive(true);
        form02a.setCreatedAt(java.time.LocalDateTime.now());
        form02a.setUpdatedAt(java.time.LocalDateTime.now());
        
        // Mẫu 02b: Văn bản đề nghị cấp lại, cấp điều chỉnh
        FormTemplate form02b = new FormTemplate();
        form02b.setFormCode("02b");
        form02b.setFormName("Văn bản đề nghị cấp lại, cấp điều chỉnh Giấy phép nhập khẩu hóa chất cấm");
        form02b.setFormType("IMPORT_CHEMICAL_PERMIT");
        form02b.setDescription("Biểu mẫu đề nghị cấp lại hoặc điều chỉnh giấy phép nhập khẩu hóa chất cấm");
        form02b.setKeywords(Arrays.asList("nhập khẩu", "hóa chất cấm", "cấp lại", "điều chỉnh", "reissue"));
        form02b.setCategories(Arrays.asList("import", "chemical", "permit", "reissue"));
        form02b.setActive(true);
        form02b.setCreatedAt(java.time.LocalDateTime.now());
        form02b.setUpdatedAt(java.time.LocalDateTime.now());
        
        // Lưu vào database
        List<FormTemplate> formsToSave = Arrays.asList(form02a, form02b);
        for (FormTemplate form : formsToSave) {
            try {
                formRepository.save(form);
                log.info("Saved form: {} ({})", form.getFormCode(), form.getFormName());
            } catch (Exception e) {
                log.error("Error saving form: {}", form.getFormCode(), e);
            }
        }
    }
}

