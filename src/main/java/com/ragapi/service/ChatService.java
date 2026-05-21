package com.ragapi.service;

import com.ragapi.dto.*;
import com.ragapi.entity.*;
import com.ragapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ChatService - Quản lý chat room và message
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final ConsultantRepository consultantRepository;
    private final ContractService contractService;

    /**
     * Gửi tin nhắn
     */
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {

        ChatRoom room = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room không tồn tại"));

        if (!"ACTIVE".equals(room.getStatus())) {
            throw new RuntimeException("Chat room đã đóng");
        }

        String content = request.getContent() != null
                ? request.getContent().trim()
                : "";

        ChatMessage message = ChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .chatRoomId(request.getChatRoomId())
                .senderId(request.getSenderId())
                .senderName(request.getSenderName())
                .senderRole(request.getSenderRole())
                .senderAvatarUrl(null)
                .content(content)
                .messageType(
                        request.getMessageType() != null
                                ? request.getMessageType()
                                : "TEXT"
                )
                .attachmentUrl(request.getAttachmentUrl())
                .attachmentName(request.getAttachmentName())
                .status("SENT")
                .sentAt(LocalDateTime.now())
                .charCount(content.length())
                .edited(false)
                .aiGenerated(false)
                .build();

        message = chatMessageRepository.save(message);

        updateChatRoomAfterMessage(room, request.getSenderRole());

        log.info("Message sent: roomId={}, messageId={}",
                room.getId(),
                message.getId());

        return mapMessageToResponse(message);
    }

    /**
     * Lấy lịch sử chat
     */
    public ChatHistoryResponse getChatHistory(
            String chatRoomId,
            int pageNumber,
            int pageSize
    ) {

        Page<ChatMessage> page = chatMessageRepository.findByChatRoomId(
                chatRoomId,
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.by(Sort.Direction.DESC, "sentAt")
                )
        );

        List<ChatMessageInfo> messages = page.getContent()
                .stream()
                .map(this::mapMessageToInfo)
                .collect(Collectors.toList());

        return ChatHistoryResponse.builder()
                .chatRoomId(chatRoomId)
                .totalMessageCount((int) page.getTotalElements())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .messages(messages)
                .build();
    }

    /**
     * Chi tiết chat room
     */
    public ChatRoomDetailResponse getChatRoomDetail(String chatRoomId) {

        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room không tồn tại"));

        return ChatRoomDetailResponse.builder()
                .chatRoomId(room.getId())
                .userId(room.getUserId())
                .userName(room.getUserName())
                .consultantId(room.getConsultantId())
                .consultantName(room.getConsultantName())
                .consultantEmail(room.getConsultantEmail())
                .consultantAvatarUrl(getConsultantAvatar(room.getConsultantId()))
                .originalQuestion(room.getOriginalQuestion())
                .aiResponse(room.getAiResponse())
                .status(room.getStatus())
                .messageCount(room.getMessageCount())
                .createdAt(room.getCreatedAt())
                .lastMessageAt(room.getLastMessageAt())
                .isUnread(room.getUnread())
                .build();
    }

    /**
     * Mark as read
     */
    public void markChatAsRead(String chatRoomId) {

        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room không tồn tại"));

        room.setUnread(false);

        chatRoomRepository.save(room);

        log.info("Chat room marked as read: {}", chatRoomId);
    }

    public void markChatAsRead(String chatRoomId, String userId) {
        markChatAsRead(chatRoomId);
    }

    public ChatClosureResponse closeChatRoom(ChatClosureRequest request) {
        return closeChatRoom(
                request.getChatRoomId(),
                request.getUserId(),
                request.getUserRating(),
                request.getUserFeedback()
        );
    }

    public ChatClosureResponse closeAndGenerateContract(ChatClosureRequest request) {
        ChatClosureResponse response = closeChatRoom(request);
        response.setDealSummary(request.getDealSummary());
        return response;
    }

    /**
     * Đóng phòng chat
     */
    public ChatClosureResponse closeChatRoom(
            String chatRoomId,
            String userId,
            Double userRating,
            String userFeedback
    ) {

        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room không tồn tại"));

        room.setStatus("CLOSED");
        room.setClosedAt(LocalDateTime.now());
        room.setFinalRating(userRating);
        room.setFinalFeedback(userFeedback);

        chatRoomRepository.save(room);

        Contract contract = contractService.createContractFromChat(chatRoomId);

        consultationRequestRepository.findByChatRoomId(chatRoomId)
                .ifPresent(request -> {

                    request.setStatus(ConsultationStatus.CONTRACT_CREATED);
                    request.setUserSatisfactionRating(userRating);
                    request.setUserFeedback(userFeedback);

                    request.setDurationSeconds(
                            (int) ChronoUnit.SECONDS.between(
                                    room.getCreatedAt(),
                                    LocalDateTime.now()
                            )
                    );

                    request.setUpdatedAt(LocalDateTime.now());

                    consultationRequestRepository.save(request);
                });

        consultantRepository.findById(room.getConsultantId())
                .ifPresent(consultant -> {

                    int currentChats = consultant.getCurrentActiveChatSessions() != null
                            ? consultant.getCurrentActiveChatSessions()
                            : 0;

                    consultant.setCurrentActiveChatSessions(
                            Math.max(0, currentChats - 1)
                    );

                    long durationHours = ChronoUnit.HOURS.between(
                            room.getCreatedAt(),
                            LocalDateTime.now()
                    );

                    consultant.setTotalHoursSpent(
                            (consultant.getTotalHoursSpent() != null
                                    ? consultant.getTotalHoursSpent()
                                    : 0) + (int) durationHours
                    );

                    consultant.setUpdatedAt(LocalDateTime.now());

                    consultantRepository.save(consultant);
                });

        log.info("Chat room closed: {}", chatRoomId);

        return ChatClosureResponse.builder()
                .chatRoomId(chatRoomId)
                .status(ChatRoomStatus.CONTRACT_CREATED.name())
                .message("Phòng chat đã được đóng và hợp đồng đã được tạo")
                .closedAt(LocalDateTime.now())
                .contractId(contract.getId())
                .contractGenerated(true)
                .paymentRequired(true)
                .contractStatus(contract.getStatus().name())
                .build();
    }

    /**
     * Lấy room chưa đọc
     */
    public List<ChatRoom> getUnreadChatRooms(String userId) {
        return chatRoomRepository.findByUserIdAndUnreadTrue(userId);
    }

    /**
     * Update room sau khi gửi tin nhắn
     */
    private void updateChatRoomAfterMessage(
            ChatRoom room,
            String senderRole
    ) {

        room.setMessageCount(
                (room.getMessageCount() != null
                        ? room.getMessageCount()
                        : 0) + 1
        );

        room.setLastMessageAt(LocalDateTime.now());

        if ("USER".equals(senderRole)) {

            room.setUserMessageCount(
                    (room.getUserMessageCount() != null
                            ? room.getUserMessageCount()
                            : 0) + 1
            );

        } else if ("CONSULTANT".equals(senderRole)) {

            room.setConsultantMessageCount(
                    (room.getConsultantMessageCount() != null
                            ? room.getConsultantMessageCount()
                            : 0) + 1
            );
        }

        room.setUnread(true);

        chatRoomRepository.save(room);
    }

    /**
     * Mapping response
     */
    private ChatMessageResponse mapMessageToResponse(ChatMessage message) {

        return ChatMessageResponse.builder()
                .messageId(message.getId())
                .chatRoomId(message.getChatRoomId())
                .senderId(message.getSenderId())
                .senderName(message.getSenderName())
                .senderRole(message.getSenderRole())
                .senderAvatarUrl(message.getSenderAvatarUrl())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .sentAt(message.getSentAt())
                .status(message.getStatus())
                .build();
    }

    private ChatMessageInfo mapMessageToInfo(ChatMessage message) {

        return ChatMessageInfo.builder()
                .messageId(message.getId())
                .senderId(message.getSenderId())
                .senderName(message.getSenderName())
                .senderRole(message.getSenderRole())
                .senderAvatarUrl(message.getSenderAvatarUrl())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .sentAt(message.getSentAt())
                .status(message.getStatus())
                .attachmentUrl(message.getAttachmentUrl())
                .attachmentName(message.getAttachmentName())
                .build();
    }

    private String getConsultantAvatar(String consultantId) {

        return consultantRepository.findById(consultantId)
                .map(Consultant::getAvatarUrl)
                .orElse(null);
    }
}
