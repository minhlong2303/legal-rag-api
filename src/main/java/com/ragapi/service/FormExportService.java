package com.ragapi.service;

import com.ragapi.entity.FormTemplate;
import com.ragapi.repository.FormTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Service để export form đã điền thành PDF hoặc Excel
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FormExportService {
    
    private final FormTemplateRepository formRepository;
    
    /**
     * Export form data thành PDF
     * Format: Tên form + Ngày tạo + Dữ liệu điền
     */
    public byte[] exportToPDF(String formId, Map<String, Object> formData) 
            throws IOException {
        log.info("Exporting form to PDF: formId={}", formId);
        
        FormTemplate form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found: " + formId));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            // Generate PDF content as string first
            String pdfContent = generatePDFContent(form, formData);
            
            // Convert string to PDF (sử dụng StringBuilder để build HTML → PDF)
            // Note: Thực tế sẽ cần iText7 library
            // Tạm thời return simple text format
            byte[] pdfBytes = pdfContent.getBytes("UTF-8");
            
            log.info("PDF exported successfully, size: {} bytes", pdfBytes.length);
            return pdfBytes;
            
        } catch (Exception e) {
            log.error("Error exporting to PDF: {}", e.getMessage(), e);
            throw new IOException("Failed to export to PDF", e);
        }
    }
    
    /**
     * Export form data thành Excel
     */
    public byte[] exportToExcel(String formId, Map<String, Object> formData) 
            throws IOException {
        log.info("Exporting form to Excel: formId={}", formId);
        
        FormTemplate form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found: " + formId));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            // Generate Excel content
            String excelContent = generateExcelContent(form, formData);
            
            // Convert to bytes
            byte[] excelBytes = excelContent.getBytes("UTF-8");
            
            log.info("Excel exported successfully, size: {} bytes", excelBytes.length);
            return excelBytes;
            
        } catch (Exception e) {
            log.error("Error exporting to Excel: {}", e.getMessage(), e);
            throw new IOException("Failed to export to Excel", e);
        }
    }
    
    /**
     * Generate PDF content as formatted text
     * Trong production, dùng iText7 hoặc PdfBox
     */
    private String generatePDFContent(FormTemplate form, Map<String, Object> formData) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("====================================================\n");
        sb.append("BIỂU MẪU: ").append(form.getFormName()).append("\n");
        sb.append("Mã biểu mẫu: ").append(form.getFormCode()).append("\n");
        sb.append("====================================================\n\n");
        
        sb.append("Ngày tạo: ").append(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n\n");
        
        sb.append("DỮ LIỆU ĐIỀN\n");
        sb.append("----------------------------------------------------\n");
        
        if (formData != null && !formData.isEmpty()) {
            formData.forEach((key, value) -> {
                sb.append(key).append(": ");
                if (value != null) {
                    sb.append(value.toString());
                } else {
                    sb.append("[không có dữ liệu]");
                }
                sb.append("\n");
            });
        }
        
        sb.append("\n====================================================\n");
        sb.append("Tài liệu này được tạo tự động bởi hệ thống RAG API\n");
        sb.append("====================================================\n");
        
        return sb.toString();
    }
    
    /**
     * Generate Excel content (CSV format)
     * Trong production, dùng Apache POI
     */
    private String generateExcelContent(FormTemplate form, Map<String, Object> formData) {
        StringBuilder sb = new StringBuilder();
        
        // CSV header
        sb.append("Biểu mẫu,").append(form.getFormName()).append("\n");
        sb.append("Mã,").append(form.getFormCode()).append("\n");
        sb.append("Ngày tạo,").append(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        sb.append("\n");
        
        // Header row
        sb.append("Trường thông tin,Giá trị\n");
        
        // Data rows
        if (formData != null && !formData.isEmpty()) {
            formData.forEach((key, value) -> {
                sb.append("\"").append(key).append("\",");
                if (value != null) {
                    sb.append("\"").append(value.toString().replace("\"", "\"\"")).append("\"");
                } else {
                    sb.append("\"[không có dữ liệu]\"");
                }
                sb.append("\n");
            });
        }
        
        return sb.toString();
    }
    
    /**
     * Get file name cho downloaded file
     */
    public String generateFileName(String formCode, String format) {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("form_%s_%s.%s", formCode, timestamp, format.toLowerCase());
    }
}

