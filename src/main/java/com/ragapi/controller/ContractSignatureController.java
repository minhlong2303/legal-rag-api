package com.ragapi.controller;

import com.ragapi.dto.SignRequest;
import com.ragapi.entity.ContractSignature;
import com.ragapi.service.ContractSignatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/contract/sign", "/api/contract/sign"})
@RequiredArgsConstructor
public class ContractSignatureController {

    private final ContractSignatureService signatureService;

    @PostMapping
    public ContractSignature sign(@Valid @RequestBody SignRequest req) {
        return signatureService.signContract(
                req.getContractId(),
                req.getUserId(),
                req.getSignerName(),
                req.getSignatureImage(),
                req.getIpAddress()
        );
    }
}
