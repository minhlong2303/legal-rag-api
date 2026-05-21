package com.ragapi.service;

import com.ragapi.dto.*;
import com.ragapi.entity.FormTemplate;
import com.ragapi.repository.FormTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service để match câu hỏi của người dùng với form phù hợp
 * Sử dụng keyword matching + semantic similarity
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FormMatcherService {
    
    private final FormTemplateRepository formRepository;
    
    /**
     * Match câu hỏi với các form phù hợp
     */
    public FormSearchResponse matchQuestionToForms(String question, String purpose) {
        log.info("Matching question to forms: {}", question);
        
        // Convert Vietnamese purpose to English (nếu cần)
        if (purpose != null) {
            purpose = convertVietnamesePurpose(purpose);
        }
        
        // Normalize question
        String normalizedQuestion = normalizeText(question);
        String[] questionTokens = normalizedQuestion.split("\\s+");
        
        // Lấy tất cả active forms
        List<FormTemplate> allForms = formRepository.findByActiveTrue();
        
        // Score mỗi form dựa trên keyword matching
        List<FormScoredMatch> scoredMatches = new ArrayList<>();
        
        for (FormTemplate form : allForms) {
            int score = calculateMatchScore(questionTokens, form, purpose);
            if (score > 0) {
                scoredMatches.add(new FormScoredMatch(form, score));
            }
        }
        
        // Sort theo score (descending)
        scoredMatches.sort((a, b) -> Integer.compare(b.score, a.score));
        
        // Chuyển đổi sang response
        List<FormSuggestion> suggestions = scoredMatches.stream()
                .limit(5)  // Chỉ suggest top 5
                .map(match -> {
                    FormSuggestion suggestion = new FormSuggestion();
                    suggestion.setFormId(match.form.getId());
                    suggestion.setFormCode(match.form.getFormCode());
                    suggestion.setFormName(match.form.getFormName());
                    suggestion.setDescription(match.form.getDescription());
                    suggestion.setMatchScore(match.score);
                    suggestion.setMatchReason(generateMatchReason(match));
                    return suggestion;
                })
                .collect(Collectors.toList());
        
        // Tạo response
        FormSearchResponse response = new FormSearchResponse();
        response.setQuestion(question);
        response.setSuggestions(suggestions);
        response.setExplanation(generateExplanation(suggestions, question));
        
        return response;
    }

    public List<FormMetadata> getAllAvailableForms() {
        return formRepository.findByActiveTrue()
                .stream()
                .map(form -> FormMetadata.builder()
                        .id(form.getId())
                        .formCode(form.getFormCode())
                        .formName(form.getFormName())
                        .description(form.getDescription())
                        .formType(form.getFormType())
                        .status(Boolean.TRUE.equals(form.getActive()) ? "ACTIVE" : "INACTIVE")
                        .build())
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate match score dựa trên:
     * 1. Keyword matching
     * 2. Category matching
     * 3. Purpose matching (nếu có)
     */
    private int calculateMatchScore(String[] questionTokens, FormTemplate form, String purpose) {
        int score = 0;
        
        // Keyword matching (mỗi keyword match = 30 points)
        List<String> formKeywords = form.getKeywords();
        if (formKeywords != null) {
            for (String token : questionTokens) {
                if (formKeywords.stream().anyMatch(kw -> similarityScore(token, kw) > 0.7)) {
                    score += 30;
                }
            }
        }
        
        // Category matching (30 points)
        if (purpose != null) {
            List<String> formCategories = form.getCategories();
            if (formCategories != null && formCategories.contains(purpose)) {
                score += 30;
            }
        }
        
        // Form type matching
        String formType = form.getFormType();
        if (formType != null) {
            if (Arrays.stream(questionTokens).anyMatch(token -> 
                    similarityScore(token, formType) > 0.6)) {
                score += 20;
            }
        }
        
        return score;
    }
    
    /**
     * Tính similarity score giữa 2 chuỗi (0-1)
     * Sử dụng Levenshtein distance
     */
    private double similarityScore(String s1, String s2) {
        String shorter = s1.length() < s2.length() ? s1 : s2;
        String longer = s1.length() < s2.length() ? s2 : s1;
        
        int minDistance = levenshteinDistance(shorter, longer);
        double similarity = 1.0 - ((double) minDistance / longer.length());
        
        return Math.max(0, similarity);
    }
    
    /**
     * Calculate Levenshtein distance
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], 
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * Normalize text (lowercase, remove diacritics, etc.)
     */
    private String normalizeText(String text) {
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
     * Generate reason text cho match
     */
    private String generateMatchReason(FormScoredMatch match) {
        StringBuilder reason = new StringBuilder();
        List<String> keywords = match.form.getKeywords();
        
        if (keywords != null && !keywords.isEmpty()) {
            reason.append("Khớp với từ khóa: ")
                    .append(String.join(", ", keywords.stream().limit(3).collect(Collectors.toList())));
        }
        
        return reason.toString();
    }
    
    /**
     * Generate explanation về kết quả tìm kiếm
     */
    private String generateExplanation(List<FormSuggestion> suggestions, String question) {
        if (suggestions.isEmpty()) {
            return "Không tìm thấy form phù hợp với câu hỏi của bạn. Vui lòng thử lại với cách diễn đạt khác.";
        }
        
        if (suggestions.size() == 1) {
            return "Tìm thấy 1 form rất phù hợp với câu hỏi của bạn.";
        }
        
        return String.format("Tìm thấy %d form phù hợp. Xin vui lòng chọn form thích hợp nhất.", 
                suggestions.size());
    }
    
    /**
     * Convert Vietnamese purpose values to English equivalents
     * Supports both Vietnamese and English values
     * 
     * Vietnamese → English mapping:
     * nhập_khẩu → import
     * xuất_khẩu → export
     * sản_xuất → production
     * lưu_trữ → storage
     * cấp_phép_lại → reissue
     * khác → other
     */
    private String convertVietnamesePurpose(String purpose) {
        if (purpose == null) {
            return null;
        }
        
        switch (purpose.toLowerCase()) {
            // Vietnamese
            case "nhập_khẩu":
            case "nhập khẩu":
                return "import";
            case "xuất_khẩu":
            case "xuất khẩu":
                return "export";
            case "sản_xuất":
            case "sản xuất":
                return "production";
            case "lưu_trữ":
            case "lưu trữ":
                return "storage";
            case "cấp_phép_lại":
            case "cấp phép lại":
                return "reissue";
            case "khác":
                return "other";
            
            // English (pass through)
            case "import":
            case "export":
            case "production":
            case "storage":
            case "reissue":
            case "other":
                return purpose.toLowerCase();
            
            // Unknown
            default:
                log.warn("Unknown purpose value: {}", purpose);
                return null;
        }
    }
    
    /**
     * Inner class để lưu form cùng score
     */
    private static class FormScoredMatch {
        FormTemplate form;
        int score;
        
        FormScoredMatch(FormTemplate form, int score) {
            this.form = form;
            this.score = score;
        }
    }
}
