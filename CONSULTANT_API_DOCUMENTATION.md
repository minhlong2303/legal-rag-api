# 🔧 Consultant System - API Documentation

## 📡 Base URL
```
http://localhost:8085/api
```

---

## 1️⃣ Consultation API

### 1.1 Offer Consultation (After 30 seconds)
**Endpoint**: `POST /consultations/offer`
**Called by**: Backend Scheduler
**Description**: Gửi offer tư vấn cho user sau 30 giây họ hỏi

**Request**:
```bash
curl -X POST "http://localhost:8085/api/consultations/offer?consultationRequestId=consul-req-001"
```

**Query Parameters**:
- `consultationRequestId` (string, required): ID của consultation request

**Response** (200 OK):
```json
{
  "consultationRequestId": "consul-req-001",
  "shouldOfferConsultation": true,
  "suggestedConsultants": [
    {
      "id": "consultant-001",
      "consultantName": "Nguyễn Văn Tư",
      "avatarUrl": "https://example.com/avatar.jpg",
      "averageRating": 4.8,
      "completedConsultations": 150,
      "description": "15+ năm kinh nghiệm trong nhập khẩu hóa chất",
      "matchScore": 95,
      "matchReason": "Chuyên môn: Nhập khẩu hóa chất, Xuất khẩu | Đánh giá: 4.8⭐",
      "responseTimeMinutes": 2,
      "specializations": ["nhập khẩu hóa chất", "xuất khẩu", "môi trường"]
    },
    {
      "id": "consultant-002",
      "consultantName": "Trần Thị Tư Vấn",
      "avatarUrl": "https://example.com/avatar2.jpg",
      "averageRating": 4.5,
      "completedConsultations": 120,
      "description": "10+ năm kinh nghiệm trong hóa chất",
      "matchScore": 85,
      "matchReason": "Chuyên môn: Hóa chất, Giấy phép | Đánh giá: 4.5⭐",
      "responseTimeMinutes": 3,
      "specializations": ["hóa chất", "giấy phép", "regulations"]
    },
    // ... more consultants (max 5)
  ],
  "message": "Bạn có muốn được tư vấn thêm từ các chuyên gia hàng đầu không?"
}
```

**Error Response** (500):
```json
{
  "error": "Consultation request not found"
}
```

---

### 1.2 Select Consultant
**Endpoint**: `POST /consultations/select`
**Called by**: Frontend (User)
**Description**: User chọn consultant và tạo chat room

**Request**:
```bash
curl -X POST "http://localhost:8085/api/consultations/select" \
  -H "Content-Type: application/json" \
  -d {
    "consultationRequestId": "consul-req-001",
    "userId": "user-123",
    "selectedConsultantId": "consultant-001"
  }
```

**Request Body**:
```json
{
  "consultationRequestId": "consul-req-001",
  "userId": "user-123",
  "selectedConsultantId": "consultant-001"
}
```

**Response** (200 OK):
```json
{
  "chatRoomId": "chat-room-001",
  "consultantName": "Nguyễn Văn Tư",
  "consultantEmail": "nguyen@consultant.com",
  "message": "Bạn sẽ được tư vấn bởi Nguyễn Văn Tư"
}
```

**Error Response** (400):
```json
{
  "error": "Consultant không tìm thấy"
}
```

---

### 1.3 Cancel Consultation Offer
**Endpoint**: `POST /consultations/cancel`
**Called by**: Frontend (User)
**Description**: User từ chối lời mời tư vấn

**Request**:
```bash
curl -X POST "http://localhost:8085/api/consultations/cancel" \
  -H "Content-Type: application/json" \
  -d {
    "consultationRequestId": "consul-req-001",
    "userId": "user-123",
    "reason": "Não preciso de consultoria agora"
  }
```

**Request Body**:
```json
{
  "consultationRequestId": "consul-req-001",
  "userId": "user-123",
  "reason": "Không cần tư vấn lúc này"
}
```

**Response** (200 OK):
```json
{
  "status": "SUCCESS",
  "message": "Lời mời tư vấn đã bị từ chối"
}
```

**Error Response** (500):
```json
{
  "error": "Error message here"
}
```

---

### 1.4 Get Consultation History
**Endpoint**: `GET /consultations/history?userId=xxx`
**Called by**: Frontend
**Description**: Lấy lịch sử tư vấn của user

**Request**:
```bash
curl -X GET "http://localhost:8085/api/consultations/history?userId=user-123"
```

**Query Parameters**:
- `userId` (string, required): User ID

**Response** (200 OK):
```json
{
  "userId": "user-123",
  "consultations": [
    {
      "id": "consul-req-001",
      "originalQuestion": "Tôi muốn nhập khẩu hóa chất cấm, cần các giấy phép gì?",
      "status": "COMPLETED",
      "assignedConsultantName": "Nguyễn Văn Tư",
      "questionAskedAt": "2026-05-18T10:00:00",
      "consultantAssignedAt": "2026-05-18T10:02:15",
      "userSatisfactionRating": 4.5,
      "userFeedback": "Tư vấn rất chi tiết và hữu ích!",
      "durationSeconds": 1200
    },
    // ... more consultations
  ],
  "count": 5
}
```

---

## 2️⃣ Chat API

### 2.1 Send Message
**Endpoint**: `POST /chat/send`
**Called by**: Frontend (User/Consultant)
**Description**: Gửi tin nhắn trong chat room

**Request**:
```bash
curl -X POST "http://localhost:8085/api/chat/send" \
  -H "Content-Type: application/json" \
  -d {
    "chatRoomId": "chat-room-001",
    "senderId": "user-123",
    "senderName": "Trần Người Dùng",
    "senderRole": "USER",
    "content": "Xin hỏi về quy trình nhập khẩu...",
    "messageType": "TEXT"
  }
```

**Request Body**:
```json
{
  "chatRoomId": "chat-room-001",
  "senderId": "user-123",
  "senderName": "Trần Người Dùng",
  "senderRole": "USER",
  "content": "Xin hỏi về quy trình nhập khẩu...",
  "messageType": "TEXT",
  "attachmentUrl": null,
  "attachmentName": null
}
```

**Response** (200 OK):
```json
{
  "messageId": "msg-001",
  "chatRoomId": "chat-room-001",
  "senderId": "user-123",
  "senderName": "Trần Người Dùng",
  "senderRole": "USER",
  "senderAvatarUrl": "https://example.com/avatar.jpg",
  "content": "Xin hỏi về quy trình nhập khẩu...",
  "messageType": "TEXT",
  "sentAt": "2026-05-18T10:05:00",
  "status": "SENT"
}
```

---

### 2.2 Get Chat History
**Endpoint**: `GET /chat/history?chatRoomId=xxx&page=0&size=50`
**Called by**: Frontend
**Description**: Lấy lịch sử chat (với pagination)

**Request**:
```bash
curl -X GET "http://localhost:8085/api/chat/history?chatRoomId=chat-room-001&page=0&size=50"
```

**Query Parameters**:
- `chatRoomId` (string, required): Chat room ID
- `page` (integer, optional, default: 0): Page number (0-indexed)
- `size` (integer, optional, default: 50): Messages per page

**Response** (200 OK):
```json
{
  "chatRoomId": "chat-room-001",
  "totalMessageCount": 12,
  "pageNumber": 0,
  "pageSize": 50,
  "messages": [
    {
      "messageId": "msg-001",
      "senderId": "consultant-001",
      "senderName": "Nguyễn Văn Tư",
      "senderRole": "CONSULTANT",
      "senderAvatarUrl": "https://example.com/avatar.jpg",
      "content": "Xin chào, tôi là Nguyễn Văn Tư, chuyên về nhập khẩu hóa chất",
      "messageType": "TEXT",
      "sentAt": "2026-05-18T10:02:45",
      "status": "READ"
    },
    {
      "messageId": "msg-002",
      "senderId": "user-123",
      "senderName": "Trần Người Dùng",
      "senderRole": "USER",
      "senderAvatarUrl": "https://example.com/user-avatar.jpg",
      "content": "Xin hỏi về giấy phép nhập khẩu...",
      "messageType": "TEXT",
      "sentAt": "2026-05-18T10:05:00",
      "status": "READ"
    }
  ]
}
```

---

### 2.3 Get Chat Room Detail
**Endpoint**: `GET /chat/detail?chatRoomId=xxx`
**Called by**: Frontend
**Description**: Lấy chi tiết phòng chat

**Request**:
```bash
curl -X GET "http://localhost:8085/api/chat/detail?chatRoomId=chat-room-001"
```

**Query Parameters**:
- `chatRoomId` (string, required): Chat room ID

**Response** (200 OK):
```json
{
  "chatRoomId": "chat-room-001",
  "userId": "user-123",
  "userName": "Trần Người Dùng",
  "consultantId": "consultant-001",
  "consultantName": "Nguyễn Văn Tư",
  "consultantEmail": "nguyen@consultant.com",
  "consultantAvatarUrl": "https://example.com/avatar.jpg",
  "originalQuestion": "Tôi muốn nhập khẩu hóa chất cấm...",
  "aiResponse": "Nhà máy xử lý chất thải cần có giấy phép môi trường...",
  "status": "ACTIVE",
  "messageCount": 12,
  "createdAt": "2026-05-18T10:02:15",
  "lastMessageAt": "2026-05-18T10:15:45",
  "isUnread": false
}
```

---

### 2.4 Mark Chat as Read
**Endpoint**: `POST /chat/mark-read`
**Called by**: Frontend
**Description**: Đánh dấu chat đã đọc

**Request**:
```bash
curl -X POST "http://localhost:8085/api/chat/mark-read" \
  -H "Content-Type: application/json" \
  -d {
    "chatRoomId": "chat-room-001",
    "userId": "user-123"
  }
```

**Request Body**:
```json
{
  "chatRoomId": "chat-room-001",
  "userId": "user-123"
}
```

**Response** (200 OK):
```json
{
  "status": "SUCCESS",
  "message": "Chat đã được đánh dấu là đã đọc"
}
```

---

### 2.5 Close Chat Room
**Endpoint**: `POST /chat/close`
**Called by**: Frontend (User)
**Description**: Đóng phòng chat và để lại đánh giá

**Request**:
```bash
curl -X POST "http://localhost:8085/api/chat/close" \
  -H "Content-Type: application/json" \
  -d {
    "chatRoomId": "chat-room-001",
    "userId": "user-123",
    "userRating": 4.5,
    "userFeedback": "Tư vấn rất chi tiết và hữu ích!"
  }
```

**Request Body**:
```json
{
  "chatRoomId": "chat-room-001",
  "userId": "user-123",
  "userRating": 4.5,
  "userFeedback": "Tư vấn rất chi tiết và hữu ích!"
}
```

**Response** (200 OK):
```json
{
  "chatRoomId": "chat-room-001",
  "status": "CLOSED",
  "message": "Phòng chat đã đóng. Cảm ơn bạn đã sử dụng dịch vụ!",
  "closedAt": "2026-05-18T10:30:00"
}
```

---

### 2.6 Get Unread Chats
**Endpoint**: `GET /chat/unread?userId=xxx`
**Called by**: Frontend
**Description**: Lấy danh sách chat chưa đọc

**Request**:
```bash
curl -X GET "http://localhost:8085/api/chat/unread?userId=user-123"
```

**Query Parameters**:
- `userId` (string, required): User ID

**Response** (200 OK):
```json
{
  "userId": "user-123",
  "unreadCount": 2,
  "chatRooms": [
    {
      "id": "chat-room-001",
      "userId": "user-123",
      "userName": "Trần Người Dùng",
      "consultantId": "consultant-001",
      "consultantName": "Nguyễn Văn Tư",
      "status": "ACTIVE",
      "messageCount": 12,
      "lastMessageAt": "2026-05-18T10:15:45",
      "isUnread": true
    }
  ]
}
```

---

## 3️⃣ Consultant Management API

### 3.1 Import Consultants from Excel
**Endpoint**: `POST /consultants/import?dryRun=true/false`
**Called by**: Frontend (Admin)
**Description**: Bulk import tài khoản consultant từ file Excel

**Request** (Multipart Form Data):
```bash
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
  -F "file=@consultants.xlsx"
```

**Query Parameters**:
- `dryRun` (boolean, optional, default: false): 
  - `true` = Preview mode (validate without saving)
  - `false` = Actual import (save to database)

**Excel Format** (16 Columns - Tiếng Việt):

| Cột | Tên Column | Kiểu | Required | Ví Dụ |
|-----|-----------|------|----------|-------|
| A | Mã Tư Vấn | Text | ✅ YES | CONS-001 |
| B | Tên Tư Vấn | Text | ✅ YES | Nguyễn Văn Tư |
| C | Email | Email | ✅ YES | nguyen@consultant.com |
| D | Điện Thoại | Text | ✅ YES | 0123456789 |
| E | Chuyên Môn | Text (phẩy ngăn cách) | ✅ YES | nhập khẩu hóa chất, xuất khẩu |
| F | Danh Mục | Text (phẩy ngăn cách) | ✅ YES | import, chemical, permit |
| G | Năm Kinh Nghiệm | Number | ❌ NO | 15 |
| H | Đánh Giá Trung Bình | Decimal (0-5) | ❌ NO | 4.8 |
| I | Số Tư Vấn Hoàn Thành | Number | ❌ NO | 150 |
| J | Thời Gian Phản Hồi (Phút) | Number | ❌ NO | 2 |
| K | Số Chat Tối Đa | Number | ❌ NO | 5 |
| L | Website | URL | ❌ NO | https://example.com |
| M | Địa Chỉ | Text | ❌ NO | 123 Đường A, Quận B |
| N | Thành Phố | Text | ❌ NO | TP.HCM |
| O | Mô Tả | Text (dài) | ❌ NO | 15+ năm kinh nghiệm... |
| P | Kích Hoạt | YES/NO | ❌ NO | YES |

**Response** (200 OK - Dry Run):
```json
{
  "success": false,
  "message": "Import hoàn tất: 5 thành công, 2 lỗi, 7 tổng cộng",
  "totalRows": 7,
  "successCount": 5,
  "errorCount": 2,
  "dryRun": true,
  "successMessages": [
    "Row 2: Nguyễn Văn Tư - OK",
    "Row 3: Trần Thị Tư Vấn - OK",
    "Row 4: Lê Văn B - OK",
    "Row 5: Phạm Thị D - OK",
    "Row 6: Đỗ Văn E - OK"
  ],
  "errorMessages": [
    "Row 7: Email 'invalid-email' không hợp lệ",
    "Row 8: Email 'tran@consultant.com' đã tồn tại"
  ]
}
```

**Response** (200 OK - Actual Import):
```json
{
  "success": true,
  "message": "Import hoàn tất: 5 thành công, 0 lỗi, 5 tổng cộng",
  "totalRows": 5,
  "successCount": 5,
  "errorCount": 0,
  "dryRun": false,
  "successMessages": [
    "Row 2: Nguyễn Văn Tư - OK",
    "Row 3: Trần Thị Tư Vấn - OK",
    "Row 4: Lê Văn B - OK",
    "Row 5: Phạm Thị D - OK",
    "Row 6: Đỗ Văn E - OK"
  ],
  "errorMessages": []
}
```

---

### 3.2 Get Import Template
**Endpoint**: `GET /consultants/import/template`
**Called by**: Frontend (Admin)
**Description**: Tải template Excel với header tiếng Việt

**Request**:
```bash
curl -X GET "http://localhost:8085/api/consultants/import/template"
```

**Response** (200 OK):
```json
{
  "message": "Tải template Excel từ đường link này: /excel-templates/consultant-import-template.xlsx",
  "format": "Excel format (.xlsx)",
  "columns": {
    "A": "Mã Tư Vấn (Required)",
    "B": "Tên Tư Vấn (Required)",
    "C": "Email (Required)",
    "D": "Điện Thoại (Required)",
    "E": "Chuyên Môn (Required, phẩy ngăn cách)",
    "F": "Danh Mục (Required, phẩy ngăn cách)",
    "G": "Năm Kinh Nghiệm (Optional)",
    "H": "Đánh Giá Trung Bình 0-5 (Optional)",
    "I": "Số Tư Vấn Hoàn Thành (Optional)",
    "J": "Thời Gian Phản Hồi Phút (Optional)",
    "K": "Số Chat Tối Đa (Optional)",
    "L": "Website (Optional)",
    "M": "Địa Chỉ (Optional)",
    "N": "Thành Phố (Optional)",
    "O": "Mô Tả (Optional)",
    "P": "Kích Hoạt YES/NO (Optional)"
  }
}
```

---

## ⚠️ Error Handling

### Standard Error Response
```json
{
  "error": "Error message describing what went wrong",
  "timestamp": "2026-05-18T10:30:00",
  "status": 400
}
```

### Common Status Codes
- `200`: Success
- `400`: Bad Request (invalid input)
- `404`: Not Found (resource doesn't exist)
- `500`: Internal Server Error

---

## 🔄 Integration with RAG API

After user asks question on `/api/ai/query`:

```javascript
// 1. User gets RAG response
const ragResponse = await fetch('/api/ai/query', {
  method: 'POST',
  body: JSON.stringify({ question, detailLevel: 'detailed' })
});

const { answer, consultationRequestId } = await ragResponse.json();

// 2. Display AI answer
displayAnswer(answer);

// 3. Wait 30 seconds
setTimeout(() => {
  // 4. Show consultation offer (backend will trigger automatically)
  // Frontend just needs to poll or use WebSocket
  showConsultationOffer(consultationRequestId);
}, 30000);

// 5. If user clicks YES, fetch suggested consultants
// (Frontend calls GET /api/consultations/offer?consultationRequestId=xxx)

// 6. User selects consultant
// Frontend calls POST /api/consultations/select

// 7. Frontend opens chat room
```

---

**API Status**: ✅ Complete & Ready for Integration
**Last Updated**: May 18, 2026


