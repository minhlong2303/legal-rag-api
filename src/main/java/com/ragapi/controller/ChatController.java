package com.ragapi.controller;

import com.ragapi.dto.ChatClosureRequest;
import com.ragapi.dto.ChatClosureResponse;
import com.ragapi.dto.ChatHistoryResponse;
import com.ragapi.dto.ChatMarkAsReadRequest;
import com.ragapi.dto.ChatMessageRequest;
import com.ragapi.dto.ChatMessageResponse;
import com.ragapi.dto.ChatRoomDetailResponse;

import com.ragapi.service.ChatService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Send message
     */
    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> sendMessage(

            @PathVariable String roomId,

            @Valid
            @RequestBody
            ChatMessageRequest request
    ) {

        try {

            request.setChatRoomId(roomId);

            ChatMessageResponse response =
                    chatService.sendMessage(
                            request
                    );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error sending message",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * Get chat room detail
     */
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<?> getChatRoomDetail(

            @PathVariable String roomId
    ) {

        try {

            ChatRoomDetailResponse response =
                    chatService
                            .getChatRoomDetail(
                                    roomId
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error fetching room detail",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * Get chat history
     */
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getChatHistory(

            @PathVariable String roomId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "50")
            int size
    ) {

        try {

            size = Math.min(size, 100);

            ChatHistoryResponse response =
                    chatService.getChatHistory(
                            roomId,
                            page,
                            size
                    );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error fetching chat history",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * Mark room as read
     */
    @PostMapping("/rooms/read")
    public ResponseEntity<?> markChatAsRead(

            @Valid
            @RequestBody
            ChatMarkAsReadRequest request
    ) {

        try {

            chatService.markChatAsRead(
                    request.getChatRoomId(),
                    request.getUserId()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "status",
                            "SUCCESS"
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Error marking room as read",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * Close room only
     */
    @PostMapping("/rooms/{roomId}/close")
    public ResponseEntity<?> closeChatRoom(

            @PathVariable String roomId,

            @Valid
            @RequestBody
            ChatClosureRequest request
    ) {

        try {

            request.setChatRoomId(roomId);

            ChatClosureResponse response =
                    chatService.closeChatRoom(
                            request
                    );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error closing room",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * Close room and generate contract
     */
    @PostMapping(
            "/rooms/{roomId}/close-and-generate-contract"
    )
    public ResponseEntity<?> closeAndGenerateContract(

            @PathVariable String roomId,

            @Valid
            @RequestBody
            ChatClosureRequest request
    ) {

        try {

            request.setChatRoomId(roomId);

            var response =
                    chatService
                            .closeAndGenerateContract(
                                    request
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error generating contract",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * Get unread rooms
     */
    @GetMapping("/users/{userId}/unread")
    public ResponseEntity<?> getUnreadChats(

            @PathVariable String userId
    ) {

        try {

            var unreadRooms =
                    chatService
                            .getUnreadChatRooms(
                                    userId
                            );

            return ResponseEntity.ok(
                    Map.of(
                            "userId",
                            userId,
                            "unreadCount",
                            unreadRooms.size(),
                            "chatRooms",
                            unreadRooms
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Error fetching unread rooms",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }
}

