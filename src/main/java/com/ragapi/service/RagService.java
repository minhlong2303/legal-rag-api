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

        log.info("Retrieving context for question: {}", question);

        // Search in Elasticsearch
        List<String> contexts =
                vectorService.search(question);

        log.info("Retrieved {} context chunks", contexts.size());

        String context =
                String.join("\n", contexts);

        String prompt = """
                Bạn là chuyên gia pháp luật môi trường tại TP.HCM.

                Chỉ trả lời dựa trên context.

                Nếu không có thông tin thì nói:
                'Không tìm thấy thông tin phù hợp.'

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        log.info("Sending prompt to GPT...");
        String answer = chatModel.generate(prompt);
        log.info("Received answer from GPT");

        return answer;
    }
}
