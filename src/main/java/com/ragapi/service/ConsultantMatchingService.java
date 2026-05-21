package com.ragapi.service;

import com.ragapi.dto.ConsultantSuggestionDTO;
import com.ragapi.entity.Consultant;
import com.ragapi.repository.ConsultantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ConsultantMatchingService - Tìm consultant phù hợp dựa trên question
 * Sử dụng Levenshtein distance để tính độ tương đồng keyword
 */
@Slf4j
@Service
@AllArgsConstructor
public class ConsultantMatchingService {
    
    private ConsultantRepository consultantRepository;
    
    /**
     * Tìm top 5 consultant phù hợp với question
     */
    public List<ConsultantSuggestionDTO> findMatchingConsultants(String question, Integer limit) {
        try {
            // 1. Get all active consultants
            List<Consultant> activeConsultants = consultantRepository.findByActiveTrue();
            
            if (activeConsultants.isEmpty()) {
                log.warn("No active consultants found");
                return Collections.emptyList();
            }
            
            // 2. Tokenize & normalize question
            List<String> questionTokens = tokenizeAndNormalize(question);
            
            // 3. Score mỗi consultant
            Map<Consultant, Double> scoreMap = new HashMap<>();
            for (Consultant consultant : activeConsultants) {
                double score = calculateMatchScore(questionTokens, consultant);
                if (score > 0) {
                    scoreMap.put(consultant, score);
                }
            }
            
            // 4. Sort by score & return top N
            return scoreMap.entrySet().stream()
                    .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                    .limit(limit != null ? limit : 5)
                    .map(entry -> mapToDTO(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error finding matching consultants", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Calculate match score dựa trên keywords
     */
    private double calculateMatchScore(List<String> questionTokens, Consultant consultant) {
        double score = 0;
        
        // 1. Keyword matching (mỗi match = 30 points)
        if (consultant.getKeywords() != null) {
            for (String keyword : consultant.getKeywords()) {
                for (String token : questionTokens) {
                    double similarity = calculateSimilarity(token, keyword);
                    if (similarity >= 0.7) {
                        score += 30;
                        break; // Mỗi keyword chỉ tính 1 lần
                    }
                }
            }
        }
        
        // 2. Category matching (30 points)
        if (consultant.getCategories() != null) {
            for (String category : consultant.getCategories()) {
                for (String token : questionTokens) {
                    if (token.contains(category) || category.contains(token)) {
                        score += 30;
                        break;
                    }
                }
            }
        }
        
        // 3. Specialization matching (20 points)
        if (consultant.getSpecializations() != null) {
            for (String spec : consultant.getSpecializations()) {
                for (String token : questionTokens) {
                    double similarity = calculateSimilarity(token, spec);
                    if (similarity >= 0.7) {
                        score += 20;
                        break;
                    }
                }
            }
        }
        
        // 4. Rating bonus (10 points nếu rating >= 4.0)
        if (consultant.getAverageRating() != null && consultant.getAverageRating() >= 4.0) {
            score += 10;
        }
        
        // 5. Response time bonus (5 points nếu response < 5 minutes)
        if (consultant.getResponseTimeMinutes() != null && consultant.getResponseTimeMinutes() < 5) {
            score += 5;
        }
        
        // 6. Availability bonus (10 points nếu có capacity)
        if (consultant.getCurrentActiveChatSessions() < consultant.getMaxConcurrentChats()) {
            score += 10;
        }
        
        return score;
    }
    
    /**
     * Tính độ tương đồng giữa 2 strings (Levenshtein distance)
     * Range: 0-1 (1 = identical)
     */
    private double calculateSimilarity(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;
        
        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLen;
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
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * Tokenize và normalize question (lowercase, xóa dấu, remove stopwords)
     */
    private List<String> tokenizeAndNormalize(String question) {
        String normalized = removeVietnameseTones(question.toLowerCase());
        
        // Remove special characters, split by space
        String[] tokens = normalized.split("[\\s\\p{Punct}]+");
        
        Set<String> stopwords = getVietnameseStopwords();
        
        return Arrays.stream(tokens)
                .filter(token -> !token.isEmpty() && !stopwords.contains(token))
                .collect(Collectors.toList());
    }
    
    /**
     * Remove Vietnamese diacritical marks
     */
    private String removeVietnameseTones(String str) {
        String nfd = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        return nfd.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    /**
     * Danh sách stopwords tiếng Việt
     */
    private Set<String> getVietnameseStopwords() {
        return Set.of("la", "le", "lam", "dung", "co", "khong", "toi", "ban", "anh", "chi", 
                      "em", "can", "muon", "dieu", "neu", "va", "hay", "hoac", "nhu", "sau");
    }
    
    /**
     * Map từ Consultant entity sang ConsultantSuggestionDTO
     */
    private ConsultantSuggestionDTO mapToDTO(Consultant consultant, double score) {
        // Normalize score to 0-100
        double normalizedScore = Math.min(score / 2.0, 100.0);
        
        return ConsultantSuggestionDTO.builder()
                .id(consultant.getId())
                .consultantName(consultant.getConsultantName())
                .avatarUrl(consultant.getAvatarUrl())
                .averageRating(consultant.getAverageRating())
                .completedConsultations(consultant.getCompletedConsultations())
                .description(consultant.getDescription())
                .matchScore(normalizedScore)
                .matchReason(generateMatchReason(consultant))
                .responseTimeMinutes(consultant.getResponseTimeMinutes())
                .specializations(consultant.getSpecializations())
                .build();
    }
    
    /**
     * Generate match reason message
     */
    private String generateMatchReason(Consultant consultant) {
        StringBuilder reason = new StringBuilder();
        reason.append("Chuyên môn: ");
        
        if (consultant.getSpecializations() != null && !consultant.getSpecializations().isEmpty()) {
            reason.append(String.join(", ", consultant.getSpecializations().stream()
                    .limit(2).collect(Collectors.toList())));
        } else {
            reason.append("Tư vấn hóa chất");
        }
        
        if (consultant.getAverageRating() != null) {
            reason.append(" | Đánh giá: ").append(String.format("%.1f", consultant.getAverageRating())).append("⭐");
        }
        
        return reason.toString();
    }
}

