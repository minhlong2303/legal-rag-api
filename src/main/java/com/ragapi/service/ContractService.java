package com.ragapi.service;

import com.ragapi.entity.ChatRoom;
import com.ragapi.entity.ChatRoomStatus;
import com.ragapi.entity.Contract;
import com.ragapi.entity.ContractStatus;
import com.ragapi.entity.ConsultationRequest;
import com.ragapi.entity.ConsultationStatus;
import com.ragapi.entity.PaymentStatus;
import com.ragapi.repository.ChatRoomRepository;
import com.ragapi.repository.ConsultationRequestRepository;
import com.ragapi.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final LegalContractFormService legalContractFormService;

    public Contract createContractFromChat(String chatRoomId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (room.getContractId() != null) {
            return contractRepository.findById(room.getContractId())
                    .orElseGet(() -> createNewContract(room));
        }

        return contractRepository.findByChatRoomId(chatRoomId)
                .orElseGet(() -> createNewContract(room));
    }

    public Contract confirmPayment(String contractId, String paymentId) {
        Contract contract = getContract(contractId);

        contract.setPaymentStatus(PaymentStatus.CONFIRMED);
        contract.setStatus(ContractStatus.PAID);
        contract.setUpdatedAt(LocalDateTime.now());
        contract = contractRepository.save(contract);

        chatRoomRepository.findById(contract.getChatRoomId())
                .ifPresent(room -> {
                    room.setStatus(ChatRoomStatus.PAYMENT_DONE.name());
                    room.setPaymentConfirmed(true);
                    room.setPaymentConfirmationId(paymentId);
                    room.setPaymentConfirmedAt(LocalDateTime.now());
                    chatRoomRepository.save(room);
                });

        consultationRequestRepository.findById(contract.getConsultationRequestId())
                .ifPresent(request -> {
                    request.setStatus(ConsultationStatus.PAID);
                    request.setUpdatedAt(LocalDateTime.now());
                    consultationRequestRepository.save(request);
                });

        return contract;
    }

    public Contract completeContract(String contractId) {
        Contract contract = getContract(contractId);

        contract.setStatus(ContractStatus.COMPLETED);
        contract.setUpdatedAt(LocalDateTime.now());
        contract = contractRepository.save(contract);

        chatRoomRepository.findById(contract.getChatRoomId())
                .ifPresent(room -> {
                    room.setStatus(ChatRoomStatus.COMPLETED.name());
                    chatRoomRepository.save(room);
                });

        consultationRequestRepository.findById(contract.getConsultationRequestId())
                .ifPresent(request -> {
                    request.setStatus(ConsultationStatus.COMPLETED);
                    request.setCompletedAt(LocalDateTime.now());
                    request.setUpdatedAt(LocalDateTime.now());
                    consultationRequestRepository.save(request);
                });

        return contract;
    }

    public Contract getContract(String contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public Contract markPaymentPending(String contractId, String paymentId) {
        Contract contract = getContract(contractId);

        contract.setStatus(ContractStatus.PENDING_PAYMENT);
        contract.setPaymentStatus(PaymentStatus.PENDING_CONFIRMATION);
        contract.setUpdatedAt(LocalDateTime.now());
        contract = contractRepository.save(contract);

        chatRoomRepository.findById(contract.getChatRoomId())
                .ifPresent(room -> {
                    room.setStatus(ChatRoomStatus.PAYMENT_PENDING.name());
                    room.setPaymentConfirmationId(paymentId);
                    chatRoomRepository.save(room);
                });

        consultationRequestRepository.findById(contract.getConsultationRequestId())
                .ifPresent(request -> {
                    request.setStatus(ConsultationStatus.PAYMENT_PENDING);
                    request.setUpdatedAt(LocalDateTime.now());
                    consultationRequestRepository.save(request);
                });

        return contract;
    }

    private Contract createNewContract(ChatRoom room) {
        ConsultationRequest request = consultationRequestRepository
                .findById(room.getConsultationRequestId())
                .orElseThrow(() -> new RuntimeException("Consultation request not found"));

        Contract contract = Contract.builder()
                .id(UUID.randomUUID().toString())
                .consultationRequestId(request.getId())
                .chatRoomId(room.getId())
                .userId(room.getUserId())
                .consultantId(room.getConsultantId())
                .title(buildContractTitle(room))
                .content(buildContractContent(room))
                .status(ContractStatus.PENDING_PAYMENT)
                .paymentStatus(PaymentStatus.INITIATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        contract = contractRepository.save(contract);

        room.setContractId(contract.getId());
        room.setContractCreated(true);
        room.setContractCreatedAt(LocalDateTime.now());
        room.setStatus(ChatRoomStatus.CONTRACT_CREATED.name());
        chatRoomRepository.save(room);

        request.setStatus(ConsultationStatus.CONTRACT_CREATED);
        request.setUpdatedAt(LocalDateTime.now());
        consultationRequestRepository.save(request);

        legalContractFormService.generateFromChat(room.getId(), contract.getId());

        return contract;
    }

    private String buildContractTitle(ChatRoom room) {
        if (room.getTopic() != null && !room.getTopic().isBlank()) {
            return "Legal consultation - " + room.getTopic();
        }

        return "Legal consultation contract";
    }

    private String buildContractContent(ChatRoom room) {
        return """
                {
                  "scope": "Legal consultation based on the completed chat session",
                  "chatRoomId": "%s",
                  "originalQuestion": "%s",
                  "aiResponse": "%s",
                  "paymentRequired": true
                }
                """.formatted(
                room.getId(),
                room.getOriginalQuestion() != null ? room.getOriginalQuestion() : "",
                room.getAiResponse() != null ? room.getAiResponse() : ""
        );
    }
}
