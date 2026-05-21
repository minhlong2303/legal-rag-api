package com.ragapi.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VietnameseLegalChunkingService {

    private static final Pattern ARTICLE_PATTERN =
            Pattern.compile("(?=Điều\\s+\\d+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final int DEFAULT_CHUNK_SIZE = 1000;

    public List<String> chunk(String text) {
        return chunk(text, DEFAULT_CHUNK_SIZE);
    }

    public List<String> chunk(String text, int maxChunkSize) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<String> sections = splitByArticles(text.trim());

        if (sections.size() > 1) {
            return fitChunkSize(sections, maxChunkSize);
        }

        return splitByLength(text.trim(), maxChunkSize);
    }

    private List<String> splitByArticles(String text) {

        List<String> sections = new ArrayList<>();
        Matcher matcher = ARTICLE_PATTERN.matcher(text);

        int lastStart = 0;
        while (matcher.find()) {
            if (matcher.start() > lastStart) {
                sections.add(text.substring(lastStart, matcher.start()).trim());
            }
            lastStart = matcher.start();
        }

        if (lastStart < text.length()) {
            sections.add(text.substring(lastStart).trim());
        }

        return sections.stream()
                .filter(s -> !s.isBlank())
                .toList();
    }

    private List<String> fitChunkSize(List<String> sections, int maxChunkSize) {

        List<String> chunks = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        for (String section : sections) {

            if (section.length() > maxChunkSize) {
                flushBuffer(chunks, buffer);
                chunks.addAll(splitByLength(section, maxChunkSize));
                continue;
            }

            if (buffer.length() + section.length() + 2 > maxChunkSize) {
                flushBuffer(chunks, buffer);
            }

            if (!buffer.isEmpty()) {
                buffer.append("\n\n");
            }
            buffer.append(section);
        }

        flushBuffer(chunks, buffer);
        return chunks;
    }

    private void flushBuffer(List<String> chunks, StringBuilder buffer) {

        if (!buffer.isEmpty()) {
            chunks.add(buffer.toString().trim());
            buffer.setLength(0);
        }
    }

    private List<String> splitByLength(String text, int maxChunkSize) {

        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += maxChunkSize) {
            chunks.add(text.substring(i, Math.min(i + maxChunkSize, text.length())).trim());
        }

        return chunks.stream()
                .filter(s -> !s.isBlank())
                .toList();
    }
}
