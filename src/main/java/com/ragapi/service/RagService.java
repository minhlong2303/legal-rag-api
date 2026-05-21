package com.ragapi.service;

import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagService {

    private final ElasticVectorService vectorService;
    private final OpenAiChatModel chatModel;

    public String ask(String question)
            throws IOException {

        return ask(question, "normal");
    }

    public String ask(String question, String detailLevel)
            throws IOException {

        log.info("Retrieving context for question: {} (detailLevel: {})", question, detailLevel);

        // Search in Elasticsearch
        List<String> contexts =
                vectorService.search(question);

        log.info("Retrieved {} context chunks", contexts.size());

        String context =
                String.join("\n", contexts);

        // Build response instruction based on detail level
        String responseInstruction = switch (detailLevel) {
            case "brief" -> """
                    FORMAT CÂU TRẢ LỜI (BRIEF):
                    - Sử dụng heading: ## Câu trả lời
                    - Trả lời trực tiếp (1-2 câu), sử dụng **bold** cho điểm quan trọng
                    - Trích dẫn Điều/Khoản chính (nếu có): "### Trích dẫn pháp luật"
                    - Tổng cộng 3-4 đoạn, ngắn gọn dễ đọc
                    """;
            case "detailed" -> """
                    FORMAT CÂU TRẢ LỜI (DETAILED):
                    - Sử dụng heading: ## Câu trả lời (câu trả lời chính xác 1-2 câu với **bold** điểm cốt lõi)
                    - ### Giải thích chi tiết:
                      * Liệt kê từng điều khoản/quy định liên quan dưới dạng bullet points
                      * Mỗi item dùng **bold** cho tên điều khoản
                    - ### Điều kiện áp dụng:
                      1. Danh sách điều kiện (numbered list)
                      2. Các ngoại lệ hoặc trường hợp riêng biệt
                    - ### Ví dụ ứng dụng thực tế:
                      * Cấp dụng cụ thể cho hoàn cảnh liên quan
                    - ### Trích dẫn pháp luật:
                      * "Điều ... - Khoản ... - Nội dung ..."
                      * Lấy từ context, không bịa
                    """;
            default -> // normal
                    """
                    FORMAT CÂU TRẢ LỜI (NORMAL):
                    - Sử dụng heading: ## Câu trả lời (câu trả lời trực tiếp 1-2 câu, **bold** điểm cốt lõi)
                    - ### Giải thích:
                      * Bullet list các điều khoản/quy định liên quan
                      * Mỗi item dùng **bold** cho tên/số điều, khoản
                    - ### Điều kiện áp dụng:
                      1. Liệt kê điều kiện chính (numbered list)
                      2. Ngoại lệ (nếu có)
                    - ### Trích dẫn pháp luật:
                      * "Điều ... - Khoản ... - Nội dung..."
                      * Lấy từ context được cung cấp
                    """;
        };

        String prompt = """
                Bạn là trợ lý tra cứu văn bản pháp luật Việt Nam chuyên sâu.

                QUY TẮC BẮT BUỘC:
                - Chỉ trả lời bằng tiếng Việt.
                - Chỉ dựa trên ngữ cảnh (context) được cung cấp, không bịa điều luật.
                - **ĐỊNH DẠNG**: Sử dụng Markdown để format câu trả lời:
                  * Dùng ## cho heading chính (Câu trả lời)
                  * Dùng ### cho heading phụ (Giải thích, Điều kiện, Trích dẫn)
                  * Dùng **bold** (**....**) cho tên điều khoản, số điều, khoản, điểm quan trọng
                  * Dùng bullet points (*) cho danh sách
                  * Dùng numbered list (1. 2. 3.) cho các bước/điều kiện
                  * Dùng line break rõ ràng giữa các section
                - Nếu context không đủ thông tin, trả lời đúng câu:
                  "Không tìm thấy thông tin phù hợp trong văn bản đã lập chỉ mục."

                HƯỚNG DẪN FORMAT:
                %s

                NGỮ CẢNH:
                %s

                CÂU HỎI:
                %s
                """.formatted(responseInstruction, context, question);

        log.info("Sending prompt to LLM...");
        try {
            log.debug("Detail level: {}, Context size: {} bytes, Question length: {}", 
                detailLevel, context.length(), question.length());
            
            String answer = chatModel.generate(prompt);
            log.info("Received answer from AI (length: {})", answer.length());
            
            if (answer == null || answer.isEmpty()) {
                log.warn("LLM returned empty response");
                return "Lỗi máy chủ: LLM trả về kết quả rỗng.";
            }

            return answer;

        } catch (Exception e) {

            // Log full exception with detailed diagnostic info
            log.error("========== LLM API ERROR ==========");
            log.error("Error Type: {}", e.getClass().getName());
            log.error("Error Message: {}", e.getMessage());
            log.error("Root Cause: {}", getRootCause(e));
            log.error("Question: {}", question);
            log.error("Context chunks: {}", contexts.size());
            log.error("Detail Level: {}", detailLevel);
            log.error("Full Stack Trace:", e);
            log.error("====================================");

            // Return a safe Vietnamese error message to the API caller
            return "Lỗi máy chủ: Không thể gọi tới dịch vụ LLM. Vui lòng thử lại sau.\n\n[Chi tiết lỗi xem trong server logs]";
        }
    }

    /**
     * Helper method to find root cause of exception
     */
    private String getRootCause(Exception e) {
        Throwable cause = e.getCause();
        if (cause == null) {
            return e.getClass().getSimpleName();
        }
        return getRootCause((Exception) cause);
    }
    }

