# 💼 Hệ Thống Tư Vấn (Consultant System) - Complete Guide

## 📋 Tổng Quan

Hệ thống Tư Vấn cho phép người dùng được tư vấn từ các chuyên gia sau khi nhận được câu trả lời từ AI RAG.

**Quy trình:**
1. User hỏi câu hỏi
2. AI RAG trả lời
3. Sau 30 giây → Hiển thị notification: "Bạn có muốn tư vấn thêm không?"
4. Nếu YES → Hiển thị top 5 consultant phù hợp
5. User chọn consultant → Chuyển sang Chat Room 1-1
6. Chat real-time giữa User & Consultant

---

## 🔄 User Flow Diagram

```
┌─────────────────┐
│  User hỏi Q&A   │
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│  AI RAG trả lời (0s)    │
└────────┬────────────────┘
         │
         │ (chờ 30s)
         ▼
    ⏰ 30 SECONDS
         │
         ▼
┌──────────────────────────────────────┐
│  Hiển thị notification: "Tư vấn không?"│
└────────┬──────────────┬──────────────┘
         │              │
      ✅ YES           ❌ NO
         │              │
         ▼              ▼
    ┌──────────┐    ┌────────┐
    │  Request │    │ SKIP   │
    │Consultant│    └────────┘
    └────┬─────┘
         │
         ▼
    ┌──────────────────────────┐
    │ Find Top 5 Consultants   │
    │ (Keyword Matching)       │
    └────┬─────────────────────┘
         │
         ▼
    ┌──────────────────────────────────────┐
    │ Display List:                        │
    │ 1. CONS-001 (Matching: 95%)         │
    │ 2. CONS-002 (Matching: 85%)         │
    │ 3. CONS-003 (Matching: 80%)         │
    │ 4. CONS-004 (Matching: 75%)         │
    │ 5. CONS-005 (Matching: 70%)         │
    └────┬─────────────────────────────────┘
         │
      (Select)
         │
         ▼
    ┌──────────────────────────────┐
    │ Create Chat Room             │
    │ + Open WebSocket Connection  │
    └────┬────────────────────────┘
         │
         ▼
    ┌───────────────────────────────┐
    │  Chat Interface               │
    │  User ←→ Consultant (Real-time│
    │  WebSocket Connection)        │
    └───────────────────────────────┘
```

---

## 📊 Database Schema

### Collection: consultants
```json
{
  "_id": "consultant-001",
  "consultantCode": "CONS-001",
  "consultantName": "Nguyễn Tư Vấn",
  "email": "nguyen@consultant.com",
  "phone": "0123456789",
  "specializations": ["nhập khẩu hóa chất", "xuất khẩu", "môi trường"],
  "categories": ["import", "chemical", "environmental"],
  "experienceYears": 10,
  "averageRating": 4.8,
  "completedConsultations": 150,
  "isActive": true,
  "responseTimeMinutes": 2,
  "maxConcurrentChats": 5,
  "currentActiveChatSessions": 2,
  "totalHoursSpent": 1200,
  "keywords": ["nhập khẩu", "hóa chất cấm", "giấy phép", "import", "chemical"],
  "verified": true,
  "createdAt": "2026-05-16T10:00:00",
  "updatedAt": "2026-05-18T15:30:00"
}
```

### Collection: consultation_requests
```json
{
  "_id": "consul-req-001",
  "userId": "user-123",
  "userEmail": "user@example.com",
  "userName": "Trần Người Dùng",
  "originalQuestion": "Tôi muốn nhập khẩu hóa chất cấm, cần các giấy phép gì?",
  "aiResponse": "Nhà máy xử lý chất thải cần có giấy phép môi trường...",
  "status": "IN_CHAT",
  "questionAskedAt": "2026-05-18T10:00:00",
  "consultationOfferedAt": "2026-05-18T10:00:30",
  "consultantAssignedAt": "2026-05-18T10:02:15",
  "assignedConsultantId": "consultant-001",
  "assignedConsultantName": "Nguyễn Tư Vấn",
  "chatRoomId": "chat-room-001",
  "suggestedConsultants": [
    {
      "consultantId": "consultant-001",
      "consultantName": "Nguyễn Tư Vấn",
      "matchScore": 95,
      "matchReason": "Chuyên môn: Nhập khẩu hóa chất, Xuất khẩu | Đánh giá: 4.8⭐"
    }
  ],
  "userSatisfactionRating": 4.5,
  "userFeedback": "Tư vấn rất chi tiết và hữu ích!"
}
```

### Collection: chat_rooms
```json
{
  "_id": "chat-room-001",
  "userId": "user-123",
  "userName": "Trần Người Dùng",
  "consultantId": "consultant-001",
  "consultantName": "Nguyễn Tư Vấn",
  "consultationRequestId": "consul-req-001",
  "originalQuestion": "Tôi muốn nhập khẩu hóa chất cấm...",
  "status": "ACTIVE",
  "messageCount": 12,
  "userMessageCount": 6,
  "consultantMessageCount": 6,
  "createdAt": "2026-05-18T10:02:15",
  "lastMessageAt": "2026-05-18T10:15:45",
  "isUnread": false,
  "topic": "Nhập khẩu hóa chất cấm"
}
```

### Collection: chat_messages
```json
{
  "_id": "msg-001",
  "chatRoomId": "chat-room-001",
  "senderId": "user-123",
  "senderName": "Trần Người Dùng",
  "senderRole": "USER",
  "content": "Xin chào, tôi có một số câu hỏi về nhập khẩu...",
  "messageType": "TEXT",
  "status": "READ",
  "sentAt": "2026-05-18T10:02:45",
  "charCount": 58
}
```

---

## 🔧 Key Components

### 1. ConsultantMatchingService
- **Purpose**: Tìm consultant phù hợp dựa trên keyword matching
- **Algorithm**: Levenshtein distance + scoring system
- **Scoring Factors**:
  - Keyword matching: 30 points
  - Category matching: 30 points
  - Specialization matching: 20 points
  - Rating bonus: 10 points
  - Response time bonus: 5 points
  - Availability bonus: 10 points
- **Output**: Top 5 consultants với score & reason

### 2. ConsultationService
- **Purpose**: Quản lý consultation requests & lifecycle
- **Key Methods**:
  - `createConsultationRequest()` - Create request khi user hỏi
  - `offerConsultation()` - Offer sau 30s (gọi từ scheduler)
  - `selectConsultant()` - User chọn consultant, tạo chat room
  - `cancelConsultationOffer()` - User từ chối offer

### 3. ChatService
- **Purpose**: Quản lý chat messages & rooms
- **Key Methods**:
  - `sendMessage()` - Gửi tin nhắn
  - `getChatHistory()` - Lấy lịch sử chat
  - `closeChatRoom()` - Đóng phòng & cập nhật stats
  - `markChatAsRead()` - Đánh dấu đã đọc

### 4. ConsultationScheduler
- **Purpose**: Tự động offer tư vấn sau 30 giây
- **Interval**: Chạy mỗi 10 giây
- **Logic**: Kiểm tra PENDING_OFFER requests, nếu >= 30s thì offer

---

## 📡 REST API Endpoints

### Consultation APIs

#### 1. Offer Consultation (Backend call)
```
POST /api/consultations/offer?consultationRequestId=xxx

Response:
{
  "consultationRequestId": "consul-req-001",
  "shouldOfferConsultation": true,
  "suggestedConsultants": [
    {
      "id": "consultant-001",
      "consultantName": "Nguyễn Tư Vấn",
      "avatarUrl": "...",
      "averageRating": 4.8,
      "completedConsultations": 150,
      "matchScore": 95,
      "matchReason": "Chuyên môn: Nhập khẩu hóa chất, Xuất khẩu | Đánh giá: 4.8⭐",
      "responseTimeMinutes": 2,
      "specializations": ["nhập khẩu hóa chất", "xuất khẩu"]
    }
  ],
  "message": "Bạn có muốn được tư vấn thêm từ các chuyên gia hàng đầu không?"
}
```

#### 2. Select Consultant
```
POST /api/consultations/select

{
  "consultationRequestId": "consul-req-001",
  "userId": "user-123",
  "selectedConsultantId": "consultant-001"
}

Response:
{
  "chatRoomId": "chat-room-001",
  "consultantName": "Nguyễn Tư Vấn",
  "consultantEmail": "nguyen@consultant.com",
  "message": "Bạn sẽ được tư vấn bởi Nguyễn Tư Vấn"
}
```

#### 3. Cancel Consultation
```
POST /api/consultations/cancel

{
  "consultationRequestId": "consul-req-001",
  "userId": "user-123",
  "reason": "Không cần tư vấn lúc này"
}

Response:
{
  "status": "SUCCESS",
  "message": "Lời mời tư vấn đã bị từ chối"
}
```

#### 4. Get Consultation History
```
GET /api/consultations/history?userId=user-123

Response:
{
  "userId": "user-123",
  "consultations": [
    {
      "id": "consul-req-001",
      "originalQuestion": "Tôi muốn nhập khẩu hóa chất cấm...",
      "status": "COMPLETED",
      "assignedConsultantName": "Nguyễn Tư Vấn",
      "questionAskedAt": "2026-05-18T10:00:00",
      "userSatisfactionRating": 4.5,
      "userFeedback": "Tư vấn rất chi tiết!"
    }
  ],
  "count": 5
}
```

### Chat APIs

#### 1. Send Message
```
POST /api/chat/send

{
  "chatRoomId": "chat-room-001",
  "senderId": "user-123",
  "senderName": "Trần Người Dùng",
  "senderRole": "USER",
  "content": "Xin hỏi về quy trình nhập khẩu...",
  "messageType": "TEXT"
}

Response:
{
  "messageId": "msg-001",
  "chatRoomId": "chat-room-001",
  "senderId": "user-123",
  "senderRole": "USER",
  "content": "Xin hỏi về quy trình nhập khẩu...",
  "sentAt": "2026-05-18T10:05:00",
  "status": "SENT"
}
```

#### 2. Get Chat History
```
GET /api/chat/history?chatRoomId=chat-room-001&page=0&size=50

Response:
{
  "chatRoomId": "chat-room-001",
  "totalMessageCount": 12,
  "pageNumber": 0,
  "pageSize": 50,
  "messages": [
    {
      "messageId": "msg-001",
      "senderId": "consultant-001",
      "senderName": "Nguyễn Tư Vấn",
      "senderRole": "CONSULTANT",
      "content": "Xin chào, tôi rất vui được tư vấn...",
      "sentAt": "2026-05-18T10:02:45",
      "status": "READ"
    }
  ]
}
```

#### 3. Close Chat Room
```
POST /api/chat/close

{
  "chatRoomId": "chat-room-001",
  "userId": "user-123",
  "userRating": 4.5,
  "userFeedback": "Tư vấn rất chi tiết và hữu ích!"
}

Response:
{
  "chatRoomId": "chat-room-001",
  "status": "CLOSED",
  "message": "Phòng chat đã đóng. Cảm ơn bạn đã sử dụng dịch vụ!",
  "closedAt": "2026-05-18T10:30:00"
}
```

---

## 🎯 Implementation Checklist

- [x] Create entities (Consultant, ConsultationRequest, ChatRoom, ChatMessage)
- [x] Create DTOs
- [x] Create repositories
- [x] Create services (Matching, Consultation, Chat)
- [x] Create controllers (Consultation, Chat)
- [x] Create scheduler (30s delay)
- [ ] Create WebSocket config (for real-time chat)
- [ ] Create React UI components
- [ ] Seed test data (consultants)
- [ ] Write unit tests
- [ ] Integration testing
- [ ] Documentation (API docs)

---

**Status**: 🟢 **Backend 100% Done** | ⏳ **Frontend Pending**


