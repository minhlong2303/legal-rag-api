package com.ragapi.service;

import com.ragapi.entity.Contract;
import com.ragapi.entity.ContractSignature;
import com.ragapi.entity.ContractStatus;
import com.ragapi.entity.LegalContractForm;
import com.ragapi.entity.LegalContractFormStatus;
import com.ragapi.repository.ContractRepository;
import com.ragapi.repository.ContractSignatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractSignatureService {

    private final ContractRepository contractRepository;
    private final ContractSignatureRepository signatureRepository;
    private final LegalContractFormService legalContractFormService;
    private final PdfStorageService pdfStorageService;

    public ContractSignature signContract(
            String contractId,
            String userId,
            String signerName,
            String signatureImageBase64,
            String ipAddress
    ) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (Boolean.TRUE.equals(contract.getSigned())) {
            return signatureRepository.findByContractId(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract already signed"));
        }

        byte[] signatureBytes = decodeSignature(signatureImageBase64);
        String signatureFileId = pdfStorageService.storeSignatureFile(
                signatureBytes,
                contractId,
                userId
        );

        ContractSignature signature = ContractSignature.builder()
                .id(UUID.randomUUID().toString())
                .contractId(contractId)
                .userId(userId)
                .signerName(signerName)
                .signatureFileId(signatureFileId)
                .signatureHash(sha256(signatureBytes))
                .ipAddress(ipAddress)
                .signedAt(LocalDateTime.now())
                .build();

        signature = signatureRepository.save(signature);

        contract.setSigned(true);
        contract.setSignedAt(signature.getSignedAt());
        contract.setSignedByUserId(userId);
        contract.setStatus(ContractStatus.SIGNED);
        contract.setUpdatedAt(LocalDateTime.now());
        contractRepository.save(contract);

        LegalContractForm form = legalContractFormService.getByContractId(contractId);
        legalContractFormService.updateStatus(
                form.getId(),
                LegalContractFormStatus.SIGNED
        );

        return signature;
    }

    private byte[] decodeSignature(String signatureImageBase64) {
        if (signatureImageBase64 == null || signatureImageBase64.isBlank()) {
            return new byte[0];
        }

        String payload = signatureImageBase64;
        int commaIndex = signatureImageBase64.indexOf(',');
        if (commaIndex >= 0) {
            payload = signatureImageBase64.substring(commaIndex + 1);
        }

        return Base64.getDecoder().decode(payload);
    }

    private String sha256(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to hash signature", ex);
        }
    }
}
