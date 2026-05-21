# 🚀 Consultant System - Implementation Guide

## 📋 Implementation Checklist

### Phase 1: Backend Setup ✅ COMPLETE
- [x] Create Consultant entity
- [x] Create ConsultationRequest entity
- [x] Create ChatRoom entity
- [x] Create ChatMessage entity
- [x] Create DTOs (all consultation & chat DTOs)
- [x] Create Repositories (4 repositories)
- [x] Create ConsultantMatchingService
- [x] Create ConsultationService
- [x] Create ChatService
- [x] Create ConsultationController
- [x] Create ChatController
- [x] Create ConsultationScheduler

### Phase 2: Database Setup ⏳ TODO
- [ ] Create MongoDB indexes
- [ ] Seed test consultants
- [ ] Configure MongoDB connection

### Phase 3: Frontend Development ⏳ TODO
- [ ] Create React components (offer, list, chat)
- [ ] Implement CSS styling
- [ ] Setup WebSocket for real-time chat
- [ ] Integrate with RAG API response

### Phase 4: Testing ⏳ TODO
- [ ] Unit tests for services
- [ ] Integration tests
- [ ] End-to-end flow testing
- [ ] Performance testing

### Phase 5: Production ⏳ TODO
- [ ] Security review
- [ ] Load testing
- [ ] Documentation
- [ ] Deployment

---

## 🛠️ Step-by-Step Implementation

### Step 1: Run Database Setup

#### Create MongoDB Indexes
```javascript
// In MongoDB console or MongoDB client
db.consultants.createIndex({ "isActive": 1, "categories": 1 });
db.consultants.createIndex({ "keywords": 1 });
db.consultants.createIndex({ "averageRating": -1 });

db.consultation_requests.createIndex({ "userId": 1, "status": 1 });
db.consultation_requests.createIndex({ "status": 1, "consultationOfferedAt": 1 });

db.chat_rooms.createIndex({ "userId": 1, "status": 1 });
db.chat_rooms.createIndex({ "consultantId": 1 });

db.chat_messages.createIndex({ "chatRoomId": 1, "sentAt": -1 });
```

#### Seed Test Data (MongoDB)
```javascript
// Insert test consultants
db.consultants.insertMany([
  {
    _id: ObjectId(),
    consultantCode: "CONS-001",
    consultantName: "Nguyễn Văn Tư",
    email: "nguyen@consultant.com",
    phone: "0123456789",
    specializations: ["nhập khẩu hóa chất", "xuất khẩu", "môi trường"],
    categories: ["import", "chemical", "environmental"],
    experienceYears: 15,
    averageRating: 4.8,
    completedConsultations: 150,
    isActive: true,
    responseTimeMinutes: 2,
    maxConcurrentChats: 5,
    currentActiveChatSessions: 0,
    totalHoursSpent: 1200,
    keywords: ["nhập khẩu", "hóa chất cấm", "giấy phép", "import", "chemical", "permit"],
    verified: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    consultantCode: "CONS-002",
    consultantName: "Trần Thị Tư Vấn",
    email: "tran@consultant.com",
    phone: "0987654321",
    specializations: ["hóa chất", "giấy phép", "regulations"],
    categories: ["chemical", "permit", "regulatory"],
    experienceYears: 10,
    averageRating: 4.5,
    completedConsultations: 120,
    isActive: true,
    responseTimeMinutes: 3,
    maxConcurrentChats: 4,
    currentActiveChatSessions: 0,
    totalHoursSpent: 900,
    keywords: ["hóa chất", "giấy phép", "regulations", "compliance"],
    verified: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }
  // ... thêm consultant khác
]);
```

---

### Step 2: Update RagController to Trigger Consultation

**File**: `src/main/java/com/ragapi/controller/RagController.java`

Thêm code này vào method `query()`:

```java
// ...existing code...

@PostMapping("/query")
public ResponseEntity<?> query(@RequestBody AiQueryRequest request) {
    try {
        // ... existing RAG logic ...
        
        // Get RAG response
        String answer = ragService.ask(request.getQuestion(), request.getDetailLevel());
        
        // Create consultation request (will be offered after 30s by scheduler)
        if (request.getUserId() != null) {
            consultationService.createConsultationRequest(
                request.getUserId(),
                request.getUserEmail(),
                request.getUserName(),
                request.getQuestion(),
                answer
            );
        }
        
        return ResponseEntity.ok(new AiQueryResponse(answer, ...));
    } catch (Exception e) {
        // ...error handling...
    }
}

// ...existing code...
```

**Add imports**:
```java
import com.ragapi.service.ConsultationService;
```

**Add dependency**:
```java
@Autowired
private ConsultationService consultationService;
```

---

### Step 3: Implement WebSocket for Real-time Chat

**File**: `src/main/java/com/ragapi/config/WebSocketConfig.java`

```java
package com.ragapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat").setAllowedOrigins("*").withSockJS();
    }
}
```

**File**: `src/main/java/com/ragapi/controller/ChatWebSocketController.java`

```java
package com.ragapi.controller;

import com.ragapi.dto.ChatMessageRequest;
import com.ragapi.dto.ChatMessageResponse;
import com.ragapi.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@AllArgsConstructor
public class ChatWebSocketController {
    
    private ChatService chatService;
    
    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatMessageResponse sendChatMessage(
            ChatMessageRequest request,
            @DestinationVariable String chatRoomId) {
        
        request.setChatRoomId(chatRoomId);
        return chatService.sendMessage(request);
    }
}
```

---

### Step 4: Update pom.xml (Add Dependencies)

```xml
<!-- WebSocket -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>

<!-- Messaging (for STOMP) -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-messaging</artifactId>
</dependency>
```

---

### Step 5: Create React Components

**File**: `frontend/src/components/ConsultationOffer.jsx`

(Copy from CONSULTANT_UI_GUIDE.md - ConsultationOfferComponent section)

**File**: `frontend/src/components/ConsultantList.jsx`

(Copy from CONSULTANT_UI_GUIDE.md - ConsultantListComponent section)

**File**: `frontend/src/components/ChatRoom.jsx`

(Copy from CONSULTANT_UI_GUIDE.md - ChatRoomComponent section + update WebSocket connection)

---

### Step 6: Integrate with RAG API Response

**File**: `frontend/src/pages/QueryPage.jsx`

```jsx
import React, { useState } from 'react';
import { ConsultationOfferComponent } from '../components/ConsultationOffer';

export const QueryPage = () => {
  const [question, setQuestion] = useState('');
  const [response, setResponse] = useState(null);
  const [consultationRequestId, setConsultationRequestId] = useState(null);

  const handleAskQuestion = async () => {
    try {
      const res = await fetch('/api/ai/query', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          question,
          userId: 'user-123', // Get from auth
          detailLevel: 'detailed'
        })
      });

      const data = await res.json();
      setResponse(data);
      setConsultationRequestId(data.consultationRequestId);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div>
      {/* Question Input */}
      <textarea 
        value={question} 
        onChange={(e) => setQuestion(e.target.value)}
        placeholder="Ask your question..."
      />
      <button onClick={handleAskQuestion}>Ask</button>

      {/* RAG Response */}
      {response && (
        <div className="response-container">
          <h3>Answer from AI:</h3>
          <div dangerouslySetInnerHTML={{ __html: marked(response.answer) }} />
        </div>
      )}

      {/* Consultation Offer (After 30s) */}
      {consultationRequestId && (
        <ConsultationOfferComponent 
          consultationRequestId={consultationRequestId}
          userId="user-123"
          question={question}
          aiResponse={response?.answer}
        />
      )}
    </div>
  );
};
```

---

### Step 7: Build & Test

```bash
# Build backend
cd D:\legal-rag-api-main\legal-rag-api-main
mvn clean package -DskipTests

# Build frontend (if using React)
cd frontend
npm install
npm start

# Run application
java -jar target/RAG_API-1.0.0.jar
```

---

### Step 8: Test End-to-End Flow

```bash
# 1. Ask a question via RAG API
curl -X POST "http://localhost:8085/api/ai/query" \
  -H "Content-Type: application/json" \
  -d {
    "question": "Tôi muốn nhập khẩu hóa chất cấm, cần những giấy phép gì?",
    "userId": "user-123",
    "detailLevel": "detailed"
  }

# 2. Wait 30 seconds (scheduler auto-offers)

# 3. Check consultation offer
curl -X GET "http://localhost:8085/api/consultations/history?userId=user-123"

# 4. Select consultant
curl -X POST "http://localhost:8085/api/consultations/select" \
  -H "Content-Type: application/json" \
  -d {
    "consultationRequestId": "XXXX",
    "userId": "user-123",
    "selectedConsultantId": "CONS-001"
  }

# 5. Send chat message
curl -X POST "http://localhost:8085/api/chat/send" \
  -H "Content-Type: application/json" \
  -d {
    "chatRoomId": "chat-room-001",
    "senderId": "user-123",
    "senderName": "User",
    "senderRole": "USER",
    "content": "Xin hỏi về quy trình...",
    "messageType": "TEXT"
  }

# 6. Close chat
curl -X POST "http://localhost:8085/api/chat/close" \
  -H "Content-Type: application/json" \
  -d {
    "chatRoomId": "chat-room-001",
    "userId": "user-123",
    "userRating": 4.5,
    "userFeedback": "Rất hữu ích!"
  }
```

---

## 📊 Performance Optimization

### Database Indexes
```javascript
// Tạo compound indexes cho query optimization
db.consultation_requests.createIndex({ "userId": 1, "consultantAssignedAt": -1 });
db.chat_messages.createIndex({ "chatRoomId": 1, "sentAt": -1 });
```

### Caching
```java
// Add Redis caching cho consultant list
@Cacheable(value = "consultants", key = "#category")
public List<Consultant> getConsultantsByCategory(String category) {
    return consultantRepository.findByCategories(category);
}
```

### Connection Pooling
```yaml
# application.yml
spring:
  data:
    mongodb:
      connection-string: mongodb://localhost:27017/ragapi
      auto-index-creation: true
```

---

## 🔒 Security Considerations

1. **Authentication**: Xác minh userId từ JWT token
2. **Authorization**: Check xem user có quyền access chat room này không
3. **Rate Limiting**: Giới hạn số message/giây
4. **Input Validation**: Validate tất cả inputs
5. **Data Encryption**: Encrypt sensitive data trong DB

---

## 📝 Completed Files

✅ **Entities** (4 files):
- Consultant.java
- ConsultationRequest.java
- ChatRoom.java
- ChatMessage.java

✅ **DTOs** (2 files):
- ConsultantDTOs.java
- ChatDTOs.java

✅ **Repositories** (4 files):
- ConsultantRepository.java
- ConsultationRequestRepository.java
- ChatRoomRepository.java
- ChatMessageRepository.java

✅ **Services** (3 files):
- ConsultantMatchingService.java
- ConsultationService.java
- ChatService.java

✅ **Controllers** (2 files):
- ConsultationController.java
- ChatController.java

✅ **Config** (1 file):
- ConsultationScheduler.java

✅ **Documentation** (4 files):
- CONSULTANT_SYSTEM_GUIDE.md
- CONSULTANT_API_DOCUMENTATION.md
- CONSULTANT_UI_GUIDE.md
- CONSULTANT_IMPLEMENTATION_GUIDE.md (this file)

---

## 🎯 Next Phase

After backend is complete:
1. Clone React components from UI guide
2. Setup WebSocket connection
3. Integrate with RAG API response handler
4. Test end-to-end in development environment
5. Deploy to staging
6. UAT & feedback collection
7. Production deployment

---

**Implementation Status**: ⏳ 50% Complete
**Backend**: ✅ 100% Complete
**Frontend**: ⏳ 0% (Ready to implement)
**Testing**: ⏳ 0% (Ready to test)


