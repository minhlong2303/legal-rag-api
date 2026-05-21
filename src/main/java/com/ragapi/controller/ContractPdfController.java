package com.ragapi.controller;

import com.ragapi.entity.Contract;
import com.ragapi.entity.LegalContractForm;
import com.ragapi.repository.ContractRepository;
import com.ragapi.service.ContractPdfService;
import com.ragapi.service.LegalContractFormService;
import com.ragapi.service.PdfStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"/contract/pdf", "/api/contract/pdf"})
@RequiredArgsConstructor
public class ContractPdfController {

    private final ContractPdfService pdfService;
    private final LegalContractFormService formService;
    private final ContractRepository contractRepository;
    private final PdfStorageService pdfStorageService;

    @GetMapping("/export/{contractId}")
    public ResponseEntity<?> export(@PathVariable String contractId) {
        try {
            Contract contract = contractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract not found"));

            LegalContractForm form = formService.getByContractId(contractId);
            pdfService.generateAndStore(contract, form);

            Contract updatedContract = contractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract not found"));
            GridFsResource resource = pdfStorageService.loadContractPdfByFileId(
                    updatedContract.getPdfFileId()
            );

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=contract-" + contractId + ".pdf"
                    )
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource.getInputStream().readAllBytes());
        } catch (Exception ex) {
            log.error("Error exporting contract PDF", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<?> download(@PathVariable String contractId) {
        try {
            Contract contract = contractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract not found"));

            GridFsResource resource;
            if (contract.getPdfFileId() != null) {
                resource = pdfStorageService.loadContractPdfByFileId(contract.getPdfFileId());
            } else {
                resource = pdfStorageService.loadContractPdfByContractId(contractId);
            }

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=contract-" + contractId + ".pdf"
                    )
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource.getInputStream().readAllBytes());
        } catch (Exception ex) {
            log.error("Error downloading contract PDF", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
