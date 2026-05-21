package com.ragapi.repository;

import com.ragapi.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    /**
     * Tìm tất cả chat room của user
     */
    List<ChatRoom> findByUserId(String userId);

    /**
     * Tìm tất cả chat room của consultant
     */
    List<ChatRoom> findByConsultantId(String consultantId);

    /**
     * Tìm room đang hoạt động giữa user và consultant
     */
    Optional<ChatRoom> findByUserIdAndConsultantIdAndStatus(
            String userId,
            String consultantId,
            String status
    );

    /**
     * Tìm room theo trạng thái
     */
    List<ChatRoom> findByStatus(String status);

    /**
     * Tìm room theo consultation request
     */
    Optional<ChatRoom> findByConsultationRequestId(String consultationRequestId);

    /**
     * Tìm room chưa đọc của user
     */
    List<ChatRoom> findByUserIdAndUnreadTrue(String userId);

    /**
     * Tìm room active của user
     */
    List<ChatRoom> findByUserIdAndStatus(String userId, String status);

    /**
     * Tìm room active của consultant
     */
    List<ChatRoom> findByConsultantIdAndStatus(String consultantId, String status);

    /**
     * Đếm số room active của consultant
     */
    Long countByConsultantIdAndStatus(String consultantId, String status);
}
