package com.ragapi.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.ragapi.entity.Contract;
import com.ragapi.entity.ContractStatus;
import com.ragapi.entity.LegalContractForm;
import com.ragapi.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class ContractPdfService {

    private final ContractRepository contractRepository;
    private final PdfStorageService pdfStorageService;

    public byte[] generate(Contract contract, LegalContractForm form) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document doc = new Document();
        PdfWriter.getInstance(doc, out);

        doc.open();

        doc.add(new Paragraph("HOP DONG PHAP LY"));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Tieu de: " + contract.getTitle()));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Noi dung:"));
        doc.add(new Paragraph(contract.getContent() != null ? contract.getContent() : ""));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("=== PHU LUC FORM ==="));
        doc.add(new Paragraph(form.getContent() != null ? form.getContent() : ""));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("CHU KY DIEN TU"));
        doc.add(new Paragraph("Trang thai: " + Boolean.TRUE.equals(contract.getSigned())));

        doc.close();

        return out.toByteArray();
    }

    public String generateAndStore(Contract contract, LegalContractForm form) throws Exception {
        contract.setStatus(ContractStatus.GENERATING_PDF);
        contract.setUpdatedAt(java.time.LocalDateTime.now());
        contractRepository.save(contract);

        byte[] pdfBytes = generate(contract, form);
        String fileId = pdfStorageService.storePdf(pdfBytes, contract.getId());

        contract.setPdfFileId(fileId);
        contract.setPdfUrl("/api/contract/pdf/" + contract.getId());
        contract.setStatus(ContractStatus.READY_FOR_SIGN);
        contract.setUpdatedAt(java.time.LocalDateTime.now());
        contractRepository.save(contract);

        return fileId;
    }
}
