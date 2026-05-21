package com.ragapi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;

@Slf4j
@Service
public class PdfExtractionService {

    @Value("${upload.pdf.max-size-mb:50}")
    private int maxSizeMb;

    @Value("${upload.pdf.min-text-length:50}")
    private int minTextLength;

    public String extractText(MultipartFile file) throws IOException {

        validatePdf(file);
        return extractText(file.getBytes(), file.getOriginalFilename());
    }

    public String extractText(byte[] pdfBytes, String fileName) throws IOException {

        log.info("Extracting text from PDF: {}", fileName);

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setLineSeparator("\n");

            String rawText = stripper.getText(document);
            String normalized = normalizeVietnameseText(rawText);

            if (normalized.length() < minTextLength) {
                throw new IllegalArgumentException(
                        "Không trích xuất được nội dung từ PDF. "
                                + "File có thể là bản scan (ảnh) — cần OCR hoặc dùng PDF có lớp văn bản."
                );
            }

            log.info(
                    "Extracted {} characters from {} pages",
                    normalized.length(),
                    document.getNumberOfPages()
            );

            return normalized;
        }
    }

    private void validatePdf(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File PDF không được để trống");
        }

        long maxBytes = (long) maxSizeMb * 1024 * 1024;
        if (file.getSize() > maxBytes) {
            throw new IllegalArgumentException(
                    "File vượt quá dung lượng cho phép: " + maxSizeMb + " MB"
            );
        }

        String name = file.getOriginalFilename();
        if (name == null || !name.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Chỉ chấp nhận file .pdf");
        }

        String contentType = file.getContentType();
        if (contentType != null
                && !contentType.equals("application/pdf")
                && !contentType.equals("application/x-pdf")) {
            throw new IllegalArgumentException("Định dạng file phải là PDF");
        }
    }

    private String normalizeVietnameseText(String text) {

        if (text == null) {
            return "";
        }

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFC);
        normalized = normalized.replace("\r\n", "\n").replace('\r', '\n');
        normalized = normalized.replaceAll("[ \t]+", " ");
        normalized = normalized.replaceAll("\n{3,}", "\n\n");

        return normalized.trim();
    }
}
