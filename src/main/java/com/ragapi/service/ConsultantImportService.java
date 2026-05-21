package com.ragapi.service;

import com.ragapi.dto.ConsultantImportResponse;
import com.ragapi.entity.Consultant;
import com.ragapi.repository.ConsultantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ConsultantImportService - Xử lý import consultant từ Excel file
 * Format: Excel (.xlsx/.xls) với 15+ columns
 */
@Slf4j
@Service
@AllArgsConstructor
public class ConsultantImportService {
    
    private ConsultantRepository consultantRepository;
    
    /**
     * Import consultant từ Excel file
     * 
     * @param file Excel file (.xlsx hoặc .xls)
     * @param dryRun true = kiểm tra lỗi không import, false = import thực tế
     * @return ConsultantImportResponse với kết quả
     */
    public ConsultantImportResponse importConsultantsFromExcel(MultipartFile file, boolean dryRun) {
        ConsultantImportResponse response = new ConsultantImportResponse();
        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        List<Consultant> consultantsToSave = new ArrayList<>();
        
        try {
            // 1. Parse Excel file
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            
            int totalRows = 0;
            int successCount = 0;
            int errorCount = 0;
            
            // 2. Bỏ qua header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                totalRows++;
                
                try {
                    // 3. Parse từng dòng thành Consultant object
                    Consultant consultant = parseConsultantFromRow(row, i + 1); // i+1 vì row index bắt đầu từ 0
                    
                    // 4. Validate consultant
                    String validationError = validateConsultant(consultant);
                    if (validationError != null) {
                        errorMessages.add(String.format("Row %d: %s", i + 1, validationError));
                        errorCount++;
                        continue;
                    }
                    
                    // 5. Check duplicate (email)
                    if (consultantRepository.findByEmail(consultant.getEmail()).isPresent()) {
                        errorMessages.add(String.format("Row %d: Email '%s' đã tồn tại", i + 1, consultant.getEmail()));
                        errorCount++;
                        continue;
                    }
                    
                    // 6. Check consultant code
                    if (consultantRepository.findByConsultantCode(consultant.getConsultantCode()).isPresent()) {
                        errorMessages.add(String.format("Row %d: Consultant Code '%s' đã tồn tại", i + 1, consultant.getConsultantCode()));
                        errorCount++;
                        continue;
                    }
                    
                    consultantsToSave.add(consultant);
                    successMessages.add(String.format("Row %d: %s - OK", i + 1, consultant.getConsultantName()));
                    successCount++;
                    
                } catch (Exception e) {
                    errorMessages.add(String.format("Row %d: Lỗi parse - %s", i + 1, e.getMessage()));
                    errorCount++;
                }
            }
            
            workbook.close();
            
            // 7. Nếu không phải dry run, lưu vào database
            if (!dryRun && errorCount == 0) {
                consultantRepository.saveAll(consultantsToSave);
                log.info("Imported {} consultants successfully", successCount);
            }
            
            response.setSuccess(errorCount == 0);
            response.setTotalRows(totalRows);
            response.setSuccessCount(successCount);
            response.setErrorCount(errorCount);
            response.setSuccessMessages(successMessages);
            response.setErrorMessages(errorMessages);
            response.setMessage(String.format("Import hoàn tất: %d thành công, %d lỗi, %d tổng cộng", 
                    successCount, errorCount, totalRows));
            response.setDryRun(dryRun);
            
        } catch (IOException e) {
            log.error("Error reading Excel file", e);
            response.setSuccess(false);
            response.setMessage("Lỗi đọc file: " + e.getMessage());
            response.getErrorMessages().add("File Excel không hợp lệ hoặc bị hỏng");
        }
        
        return response;
    }
    
    /**
     * Parse Consultant từ một row trong Excel
     * 
     * Column mapping:
     * 0 = Consultant Code
     * 1 = Consultant Name
     * 2 = Email
     * 3 = Phone
     * 4 = Specializations (comma-separated)
     * 5 = Categories (comma-separated)
     * 6 = Experience Years
     * 7 = Average Rating (0-5)
     * 8 = Completed Consultations
     * 9 = Response Time Minutes
     * 10 = Max Concurrent Chats
     * 11 = Website
     * 12 = Address
     * 13 = City
     * 14 = Description
     * 15 = Is Active (YES/NO hoặc 1/0)
     */
    private Consultant parseConsultantFromRow(Row row, int rowNumber) {
        Consultant consultant = new Consultant();
        
        // Required fields
        consultant.setId(UUID.randomUUID().toString());
        consultant.setConsultantCode(getCellValue(row, 0));
        consultant.setConsultantName(getCellValue(row, 1));
        consultant.setEmail(getCellValue(row, 2));
        consultant.setPhone(getCellValue(row, 3));
        
        // Specializations (comma-separated)
        String specializationStr = getCellValue(row, 4);
        if (specializationStr != null && !specializationStr.isEmpty()) {
            List<String> specs = Arrays.stream(specializationStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            consultant.setSpecializations(specs);
        }
        
        // Categories (comma-separated)
        String categoriesStr = getCellValue(row, 5);
        if (categoriesStr != null && !categoriesStr.isEmpty()) {
            List<String> cats = Arrays.stream(categoriesStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            consultant.setCategories(cats);
        }
        
        // Experience years
        String expStr = getCellValue(row, 6);
        if (expStr != null && !expStr.isEmpty()) {
            try {
                consultant.setExperienceYears(Integer.parseInt(expStr.trim()));
            } catch (NumberFormatException e) {
                consultant.setExperienceYears(0);
            }
        }
        
        // Rating
        String ratingStr = getCellValue(row, 7);
        if (ratingStr != null && !ratingStr.isEmpty()) {
            try {
                consultant.setAverageRating(Double.parseDouble(ratingStr.trim()));
            } catch (NumberFormatException e) {
                consultant.setAverageRating(0.0);
            }
        }
        
        // Completed consultations
        String completedStr = getCellValue(row, 8);
        if (completedStr != null && !completedStr.isEmpty()) {
            try {
                consultant.setCompletedConsultations(Integer.parseInt(completedStr.trim()));
            } catch (NumberFormatException e) {
                consultant.setCompletedConsultations(0);
            }
        }
        
        // Response time
        String responseTimeStr = getCellValue(row, 9);
        if (responseTimeStr != null && !responseTimeStr.isEmpty()) {
            try {
                consultant.setResponseTimeMinutes(Integer.parseInt(responseTimeStr.trim()));
            } catch (NumberFormatException e) {
                consultant.setResponseTimeMinutes(5);
            }
        }
        
        // Max concurrent chats
        String maxChatsStr = getCellValue(row, 10);
        if (maxChatsStr != null && !maxChatsStr.isEmpty()) {
            try {
                consultant.setMaxConcurrentChats(Integer.parseInt(maxChatsStr.trim()));
            } catch (NumberFormatException e) {
                consultant.setMaxConcurrentChats(5);
            }
        }
        
        // Optional fields
        consultant.setWebsite(getCellValue(row, 11));
        consultant.setAddress(getCellValue(row, 12));
        consultant.setCity(getCellValue(row, 13));
        consultant.setDescription(getCellValue(row, 14));
        
        // Is active
        String activeStr = getCellValue(row, 15);
        Boolean isActive = true;
        if (activeStr != null) {
            String active = activeStr.trim().toLowerCase();
            isActive = active.equals("yes") || active.equals("true") || active.equals("1");
        }
        consultant.setActive(isActive);
        
        // Keywords (auto-generate từ specializations + categories)
        List<String> keywords = generateKeywords(consultant);
        consultant.setKeywords(keywords);
        
        // Metadata
        consultant.setCurrentActiveChatSessions(0);
        consultant.setTotalHoursSpent(0);
        consultant.setVerified(true); // Assume verified khi import từ admin
        consultant.setCreatedAt(LocalDateTime.now());
        consultant.setUpdatedAt(LocalDateTime.now());
        consultant.setCreatedBy("ADMIN_IMPORT");
        
        return consultant;
    }
    
    /**
     * Validate Consultant object
     * @return Error message nếu invalid, null nếu valid
     */
    private String validateConsultant(Consultant consultant) {
        // Code required
        if (consultant.getConsultantCode() == null || consultant.getConsultantCode().trim().isEmpty()) {
            return "Consultant Code không được để trống";
        }
        
        // Name required
        if (consultant.getConsultantName() == null || consultant.getConsultantName().trim().isEmpty()) {
            return "Consultant Name không được để trống";
        }
        
        // Email required & valid
        if (consultant.getEmail() == null || consultant.getEmail().trim().isEmpty()) {
            return "Email không được để trống";
        }
        if (!consultant.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Email không hợp lệ: " + consultant.getEmail();
        }
        
        // Phone required
        if (consultant.getPhone() == null || consultant.getPhone().trim().isEmpty()) {
            return "Phone không được để trống";
        }
        
        // At least 1 specialization
        if (consultant.getSpecializations() == null || consultant.getSpecializations().isEmpty()) {
            return "Chuyên môn (Specialization) không được để trống";
        }
        
        // At least 1 category
        if (consultant.getCategories() == null || consultant.getCategories().isEmpty()) {
            return "Danh mục (Category) không được để trống";
        }
        
        // Rating valid
        if (consultant.getAverageRating() != null && (consultant.getAverageRating() < 0 || consultant.getAverageRating() > 5)) {
            return "Average Rating phải trong khoảng 0-5";
        }
        
        return null; // Valid
    }
    
    /**
     * Auto-generate keywords từ specializations + categories
     */
    private List<String> generateKeywords(Consultant consultant) {
        Set<String> keywords = new HashSet<>();
        
        if (consultant.getSpecializations() != null) {
            keywords.addAll(consultant.getSpecializations());
        }
        
        if (consultant.getCategories() != null) {
            keywords.addAll(consultant.getCategories());
        }
        
        if (consultant.getConsultantName() != null) {
            keywords.add(consultant.getConsultantName());
        }
        
        return new ArrayList<>(keywords);
    }
    
    /**
     * Get cell value as String (xử lý tất cả các loại cell)
     */
    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}

