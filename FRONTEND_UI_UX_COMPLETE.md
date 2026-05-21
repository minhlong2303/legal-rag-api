# 🎨 FRONTEND UI/UX COMPLETE GUIDE

**For: React/Vue/Angular Frontend Development Team**  
**Last Updated: May 20, 2026**  
**Status: ✅ Ready for Implementation**

---

## 📑 Table of Contents
1. [System Overview](#system-overview)
2. [Component Architecture](#component-architecture)
3. [Features & Flows](#features--flows)
4. [API Integration](#api-integration)
5. [React Components (Copy-Paste Ready)](#react-components-copy-paste-ready)
6. [Styling Guidelines](#styling-guidelines)
7. [State Management](#state-management)
8. [Testing Checklist](#testing-checklist)

---

## 🎯 System Overview

### Three Main Features:
**1️⃣ RAG Chat (Legal Document Q&A)**
- User asks question about legal/chemical documents
- AI responds with relevant information
- After 30 seconds → System offers expert consultation

**2️⃣ Expert Consultant System**
- Shows top 5 matching consultants
- Real-time 1-1 chat between user and consultant
- Rating & feedback system

**3️⃣ Form Submission System**
- Dynamic forms based on user question
- Auto-fill user data from profile
- Multi-step form workflow

---

## 🏗️ Component Architecture

### Navigation Flow
```
┌─────────────────────────────────────────────┐
│          Main Application Wrapper           │
├─────────────────────────────────────────────┤
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │         Navigation/Header             │  │
│  │  Logo | Home | Chat | Consultants    │  │
│  └──────────────────────────────────────┘  │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │        Main Content Area             │  │
│  │  ┌──────────┬─────────┬────────────┐ │  │
│  │  │ RAG Chat │Consult  │ Forms      │ │  │
│  │  └──────────┴─────────┴────────────┘ │  │
│  └──────────────────────────────────────┘  │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │    Footer / Support Info             │  │
│  └──────────────────────────────────────┘  │
│                                             │
└─────────────────────────────────────────────┘
```

### Component Tree
```
App
├── Header
│   ├── Logo
│   ├── Navigation Menu
│   └── User Avatar / Settings
├── MainLayout
│   ├── ChatPage
│   │   ├── QuestionInput
│   │   ├── MessageHistory
│   │   ├── ConsultationOffer (appears at 30s)
│   │   │   ├── ConsultantList (top 5)
│   │   │   └── SelectConsultantModal
│   │   └── ChatRoom
│   │       ├── ChatHeader
│   │       ├── MessageList
│   │       ├── MessageInput
│   │       └── RatingPanel
│   ├── ConsultantsPage
│   │   ├── ConsultantFilter
│   │   ├── ConsultantGrid
│   │   ├── ConsultantCard
│   │   └── ConsultantDetailModal
│   ├── FormsPage
│   │   ├── FormBuilder
│   │   ├── FormStep
│   │   ├── FormPreview
│   │   └── SubmissionSuccess
│   └── AdminPage (Optional)
│       ├── ConsultantImport
│       ├── ImportProgress
│       └── ImportResults
└── Footer
```

---

## 🔄 Features & Flows

### Feature 1: RAG Chat (AI Question Answering)

**User Flow:**
```
1. User enters question
   ↓
2. AI processes and responds (real-time streaming or full response)
   ↓
3. After 30 seconds, system offers: "Muốn tư vấn thêm với chuyên gia?"
   ↓
4. User can:
   • Click "Có, tìm tư vấn" → See top 5 consultants
   • Click "Không cần" → Continue chatting
   • Ignore → Close notification
```

**UI Components:**
- **QuestionInputBox**: Text area + Submit button
- **MessageBubble**: AI response in left bubble (light gray), User Q in right bubble (blue)
- **ConsultationOfferCard**: 
  ```
  ┌──────────────────────────────┐
  │ 💡 Bạn muốn tư vấn tại chỗ?  │
  │                              │
  │ "Tôi có chuyên gia sẵn sàng   │
  │  giúp bạn..."                │
  │                              │
  │ [Không cần]  [Tìm tư vấn]    │
  └──────────────────────────────┘
  ```

**API Endpoints Used:**
- `POST /api/questions` - Submit question
- `POST /api/consultations/offer?consultationRequestId=xxx` - Trigger offer
- `GET /api/consultants/search?keywords=...` - Search consultants

---

### Feature 2: Expert Consultant System

**Flow 1: Browse & Select Consultant**
```
1. User clicks "Tìm tư vấn" or goes to Consultants page
   ↓
2. System shows top 5 matching consultants with:
   • Consultant name + avatar
   • Rating (⭐⭐⭐⭐ 4.5/5)
   • Specializations: ["Import", "Compliance"]
   • Response time: "< 5 phút"
   • Match score: "95% phù hợp"
   ↓
3. User clicks consultant card → Opens detail modal/page
   ↓
4. User clicks "Chat với chuyên gia" → Creates chat room
   ↓
5. Real-time chat opens with consultant
```

**Flow 2: Real-Time Chat**
```
1. Chat room created with:
   • Consultant info (name, avatar, email)
   • Original question displayed
   • Message history
   ↓
2. User-Consultant messaging:
   • Text messages
   • Can share images/files
   • Status indicators (sent, delivered, read)
   ↓
3. Chat closure:
   • User clicks "Kết thúc hội thoại"
   • Shows rating panel (1-5 stars)
   • Optional feedback text
   • Submit → Chat room closes
```

**UI Components:**

**ConsultantCard:**
```jsx
┌────────────────────────────┐
│  [Avatar]                  │
│  Nguyễn Văn A             │
│  ⭐ 4.8/5 (156 reviews)    │
│                            │
│  Chuyên môn:               │
│  • Giấy phép môi trường   │
│  • Quản lý chất thải      │
│                            │
│  ⏱ Thời gian phản hồi     │
│  < 5 phút                 │
│                            │
│  Match: 95% ✓             │
│                            │
│  [Chi tiết] [Chat ngay]   │
└────────────────────────────┘
```

**ChatMessage:**
```jsx
// User message (right aligned, blue)
┌─────────────────────────────────┐
│                  Xin chào, tôi   │
│                  cần hỗ trợ       │
│             14:30 ✓✓ Đã đọc      │
└─────────────────────────────────┘

// Consultant message (left aligned, gray)
┌──────────────────────────────┐
│  Chào bạn, tôi sẵn sàng     │
│  giúp đỡ. Vui lòng cho      │
│  tôi biết chi tiết...       │
│  14:32 ✓                    │
└──────────────────────────────┘
```

**Rating Panel:**
```jsx
┌─────────────────────────────────┐
│ Đánh giá tư vấn:                │
│                                 │
│ ⭐ ⭐ ⭐ ⭐ ⭐ (5 sao)           │
│                                 │
│ Nhận xét (tùy chọn):           │
│ ┌──────────────────────────┐   │
│ │ Chuyên gia rất tốt...    │   │
│ └──────────────────────────┘   │
│                                 │
│        [Hủy]  [Gửi đánh giá]   │
└─────────────────────────────────┘
```

**API Endpoints Used:**
- `POST /api/consultations/select` - Start consultation
- `POST /api/chat/send` - Send message
- `GET /api/chat/history?chatRoomId=xxx&page=0` - Get message history
- `POST /api/chat/close` - Close chat + submit rating
- `GET /api/chat/unread?userId=xxx` - Check unread messages

---

### Feature 3: Form System

**Flow:**
```
1. System suggests: "Bạn muốn tạo biểu mẫu cấp phép?"
   ↓
2. Choose form type from AI-suggested forms
   ↓
3. Multi-step form wizard:
   • Step 1: Personal Info (auto-filled from profile)
   • Step 2: Organization Data
   • Step 3: Document Details
   • Step 4: Review & Submit
   ↓
4. Form submitted → Confirmation page
```

**Form Structure:**
```jsx
Form = {
  id: "permit_chemical_import",
  name: "Giấy phép nhập khẩu hóa chất",
  fields: [
    {
      id: "company_name",
      type: "text",
      label: "Tên công ty",
      required: true,
      autoFill: true, // From user profile
      validation: "required|min:3"
    },
    {
      id: "contact_person",
      type: "text",
      label: "Người liên hệ",
      required: true,
      autoFill: true
    },
    {
      id: "chemical_type",
      type: "select",
      label: "Loại hóa chất",
      options: ["Chất độc hại", "Chất ăn mòn", ...],
      required: true
    },
    {
      id: "documents",
      type: "file",
      label: "Tệp đính kèm",
      required: true,
      accept: ".pdf,.xlsx"
    },
    ...
  ],
  steps: 4
}
```

**UI Components:**

**FormStepIndicator:**
```
Step 1: Personal Info  ✓
Step 2: Organization   ● (current)
Step 3: Documents      ○
Step 4: Review         ○
```

**FormField Examples:**
- Text Input: `/input` with label + validation
- Select Dropdown: `/select` with options
- File Upload: `/upload` with drag-drop
- Checkbox: `/checkbox` with option
- Radio: `/radio` group
- Date Picker: `/date` with calendar

**API Endpoints Used:**
- `GET /api/forms/templates?question=...` - Get suggested forms
- `POST /api/forms/submit` - Submit form
- `GET /api/forms/fields?formId=xxx` - Get form fields
- `GET /api/user/profile` - Get auto-fill data

---

## 🔌 API Integration

### Base URL
```
Production: https://your-domain.com/api
Local Dev: http://localhost:8080/api
Staging: https://staging.your-domain.com/api
```

### Authentication
```javascript
// Add to request header
Authorization: Bearer {token}
Content-Type: application/json
```

### Error Handling
```javascript
// Standard error response
{
  "success": false,
  "message": "Lỗi: Không thể gửi tin nhắn",
  "errorCode": "CHAT_SEND_FAILED",
  "statusCode": 400
}

// Success response
{
  "success": true,
  "data": { ... },
  "message": "Thành công"
}
```

### Key API Endpoints

#### Chat APIs
```
POST   /api/questions                          // Submit Q&A question
POST   /api/consultations/offer                // Trigger consultant offer
POST   /api/consultations/select               // Select consultant
POST   /api/consultations/cancel               // Cancel offer
GET    /api/consultations/history              // Get user's consultations
POST   /api/chat/send                          // Send message (WebSocket preferred)
GET    /api/chat/history?chatRoomId=X&page=0  // Get chat history
GET    /api/chat/detail?chatRoomId=X          // Get room info
POST   /api/chat/mark-read                     // Mark as read
POST   /api/chat/close                         // Close chat + rate
GET    /api/chat/unread?userId=X              // Get unread count
```

#### Consultant APIs
```
GET    /api/consultants/search?keywords=...   // Search consultants
GET    /api/consultants                       // List all active consultants
GET    /api/consultants/{id}                  // Get consultant detail
POST   /api/consultants/import                // Admin: Import consultants
GET    /api/consultants/import/template       // Get import template
```

#### Form APIs
```
GET    /api/forms/templates?question=...      // Get form suggestions
GET    /api/forms/{formId}                    // Get form structure
POST   /api/forms/submit                      // Submit filled form
GET    /api/forms/fields                      // Get all field definitions
```

---

## 💅 React Components (Copy-Paste Ready)

### 1. QuestionInput Component

```jsx
import React, { useState } from 'react';
import axios from 'axios';

export const QuestionInput = ({ onQuestionSubmit }) => {
  const [question, setQuestion] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!question.trim()) return;

    setLoading(true);
    try {
      const response = await axios.post('/api/questions', {
        question: question.trim(),
        userId: localStorage.getItem('userId')
      });
      
      onQuestionSubmit({
        question,
        response: response.data.answer,
        requestId: response.data.consultationRequestId
      });
      
      setQuestion('');
    } catch (err) {
      setError(err.response?.data?.message || 'Lỗi khi gửi câu hỏi');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="question-input">
      <textarea
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
        placeholder="Hỏi về giấy phép, quy định, thủ tục..."
        className="question-textarea"
        rows={4}
        disabled={loading}
      />
      {error && <div className="error-message">{error}</div>}
      <button 
        type="submit" 
        disabled={loading || !question.trim()}
        className="btn-submit"
      >
        {loading ? 'Đang xử lý...' : 'Gửi câu hỏi'}
      </button>
    </form>
  );
};
```

### 2. ConsultationOffer Component

```jsx
import React, { useEffect, useState } from 'react';
import { ConsultantSelector } from './ConsultantSelector';

export const ConsultationOffer = ({ 
  consultationRequestId, 
  onAccept, 
  onDecline 
}) => {
  const [showOffer, setShowOffer] = useState(false);
  const [suggestedConsultants, setSuggestedConsultants] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Show offer after 30 seconds
    const timer = setTimeout(async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `/api/consultations/offer?consultationRequestId=${consultationRequestId}`,
          { method: 'POST' }
        );
        const data = await response.json();
        
        if (data.suggestedConsultants?.length > 0) {
          setSuggestedConsultants(data.suggestedConsultants);
          setShowOffer(true);
        }
      } catch (err) {
        console.error('Error fetching consultants:', err);
      } finally {
        setLoading(false);
      }
    }, 30000); // 30 seconds

    return () => clearTimeout(timer);
  }, [consultationRequestId]);

  if (!showOffer || loading) return null;

  return (
    <div className="consultation-offer-container">
      <div className="offer-card">
        <h3>💡 Bạn muốn tư vấn trực tiếp?</h3>
        <p>Chúng tôi có các chuyên gia phù hợp sẵn sàng hỗ trợ bạn</p>
        
        <ConsultantSelector 
          consultants={suggestedConsultants}
          onSelectConsultant={onAccept}
        />
        
        <button 
          onClick={onDecline}
          className="btn-secondary"
        >
          Không cần, cảm ơn
        </button>
      </div>
    </div>
  );
};
```

### 3. ConsultantCard Component

```jsx
import React from 'react';

export const ConsultantCard = ({ 
  consultant, 
  onSelect, 
  showMatchScore = true 
}) => {
  const {
    id,
    consultantName,
    avatarUrl,
    averageRating,
    completedConsultations,
    specializations = [],
    responseTimeMinutes,
    matchScore,
    matchReason
  } = consultant;

  return (
    <div className="consultant-card">
      <div className="consultant-header">
        <img 
          src={avatarUrl || '/default-avatar.png'} 
          alt={consultantName}
          className="consultant-avatar"
        />
        <div className="consultant-info">
          <h4>{consultantName}</h4>
          <div className="rating">
            <span className="stars">{'⭐'.repeat(Math.round(averageRating))}</span>
            <span className="rating-value">{averageRating?.toFixed(1) || 'N/A'}/5</span>
            <span className="review-count">({completedConsultations} đánh giá)</span>
          </div>
        </div>
      </div>

      <div className="consultant-specializations">
        <h5>Chuyên môn:</h5>
        <div className="specializations-list">
          {specializations.map((spec, idx) => (
            <span key={idx} className="spec-badge">{spec}</span>
          ))}
        </div>
      </div>

      <div className="consultant-meta">
        <div className="meta-item">
          <span className="label">⏱ Thời gian phản hồi:</span>
          <span className="value">
            {responseTimeMinutes ? `< ${responseTimeMinutes} phút` : 'Linh hoạt'}
          </span>
        </div>
        {showMatchScore && matchScore && (
          <div className="meta-item">
            <span className="label">Match Score:</span>
            <span className="value match-score">{matchScore}% ✓</span>
          </div>
        )}
      </div>

      {matchReason && (
        <div className="match-reason">
          <p>"{matchReason}"</p>
        </div>
      )}

      <button 
        onClick={() => onSelect(consultant)}
        className="btn-primary btn-select"
      >
        Chat ngay
      </button>
    </div>
  );
};
```

### 4. ChatRoom Component

```jsx
import React, { useState, useEffect, useRef } from 'react';
import { RatingPanel } from './RatingPanel';

export const ChatRoom = ({ 
  chatRoomId, 
  consultantName,
  onClose 
}) => {
  const [messages, setMessages] = useState([]);
  const [inputValue, setInputValue] = useState('');
  const [loading, setLoading] = useState(false);
  const [showRating, setShowRating] = useState(false);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    // Load chat history
    const loadHistory = async () => {
      try {
        const response = await fetch(
          `/api/chat/history?chatRoomId=${chatRoomId}&page=0&size=50`
        );
        const data = await response.json();
        setMessages(data.messages || []);
      } catch (err) {
        console.error('Error loading chat history:', err);
      }
    };

    loadHistory();
  }, [chatRoomId]);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!inputValue.trim()) return;

    const messageContent = inputValue;
    setInputValue('');
    setLoading(true);

    try {
      const response = await fetch('/api/chat/send', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          chatRoomId,
          senderId: localStorage.getItem('userId'),
          senderName: localStorage.getItem('userName'),
          senderRole: 'USER',
          content: messageContent,
          messageType: 'TEXT'
        })
      });

      const newMessage = await response.json();
      setMessages([...messages, newMessage]);
    } catch (err) {
      console.error('Error sending message:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="chat-room">
      <div className="chat-header">
        <h3>Chat với {consultantName}</h3>
        <button 
          onClick={() => setShowRating(true)}
          className="btn-close"
          title="Kết thúc hội thoại"
        >
          ✕
        </button>
      </div>

      <div className="messages-container">
        {messages.map((msg, idx) => (
          <div 
            key={idx}
            className={`message ${msg.senderRole === 'USER' ? 'user' : 'consultant'}`}
          >
            <div className="message-content">{msg.content}</div>
            <div className="message-meta">
              <span className="time">{new Date(msg.sentAt).toLocaleTimeString('vi-VN')}</span>
              {msg.senderRole === 'USER' && (
                <span className="status">{msg.status === 'READ' ? '✓✓' : '✓'}</span>
              )}
            </div>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      {!showRating ? (
        <form onSubmit={handleSendMessage} className="message-input-form">
          <input
            type="text"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            placeholder="Nhập tin nhắn..."
            disabled={loading}
            className="message-input"
          />
          <button 
            type="submit"
            disabled={loading || !inputValue.trim()}
            className="btn-send"
          >
            Gửi
          </button>
        </form>
      ) : (
        <RatingPanel 
          onSubmit={(rating, feedback) => {
            // Call API to close chat
            onClose(rating, feedback);
          }}
          onCancel={() => setShowRating(false)}
        />
      )}
    </div>
  );
};
```

### 5. RatingPanel Component

```jsx
import React, { useState } from 'react';

export const RatingPanel = ({ onSubmit, onCancel }) => {
  const [rating, setRating] = useState(0);
  const [feedback, setFeedback] = useState('');

  const stars = [1, 2, 3, 4, 5];

  return (
    <div className="rating-panel">
      <h4>Đánh giá tư vấn của bạn</h4>
      
      <div className="stars-selector">
        {stars.map(star => (
          <button
            key={star}
            className={`star ${rating >= star ? 'active' : ''}`}
            onClick={() => setRating(star)}
          >
            ⭐
          </button>
        ))}
      </div>

      <div className="form-group">
        <label>Nhận xét (tùy chọn)</label>
        <textarea
          value={feedback}
          onChange={(e) => setFeedback(e.target.value)}
          placeholder="Chia sẻ trải nghiệm của bạn..."
          className="feedback-textarea"
          rows={3}
        />
      </div>

      <div className="button-group">
        <button 
          onClick={onCancel}
          className="btn-secondary"
        >
          Hủy
        </button>
        <button 
          onClick={() => onSubmit(rating, feedback)}
          disabled={rating === 0}
          className="btn-primary"
        >
          Gửi đánh giá
        </button>
      </div>
    </div>
  );
};
```

### 6. FormField Component

```jsx
import React from 'react';

export const FormField = ({ 
  field, 
  value, 
  onChange, 
  error 
}) => {
  const {
    id,
    type,
    label,
    placeholder,
    required,
    options = [],
    validate,
    helperText
  } = field;

  const renderField = () => {
    switch (type) {
      case 'text':
        return (
          <input
            type="text"
            id={id}
            value={value || ''}
            onChange={(e) => onChange(id, e.target.value)}
            placeholder={placeholder}
            className={`form-input ${error ? 'error' : ''}`}
          />
        );

      case 'email':
        return (
          <input
            type="email"
            id={id}
            value={value || ''}
            onChange={(e) => onChange(id, e.target.value)}
            placeholder={placeholder}
            className={`form-input ${error ? 'error' : ''}`}
          />
        );

      case 'select':
        return (
          <select
            id={id}
            value={value || ''}
            onChange={(e) => onChange(id, e.target.value)}
            className={`form-select ${error ? 'error' : ''}`}
          >
            <option value="">-- Chọn --</option>
            {options.map((opt, idx) => (
              <option key={idx} value={opt.value || opt}>
                {opt.label || opt}
              </option>
            ))}
          </select>
        );

      case 'textarea':
        return (
          <textarea
            id={id}
            value={value || ''}
            onChange={(e) => onChange(id, e.target.value)}
            placeholder={placeholder}
            className={`form-textarea ${error ? 'error' : ''}`}
            rows={4}
          />
        );

      case 'file':
        return (
          <div className="file-upload">
            <input
              type="file"
              id={id}
              onChange={(e) => onChange(id, e.target.files[0])}
              className={`form-file ${error ? 'error' : ''}`}
            />
            <label htmlFor={id} className="file-label">
              📎 Chọn tệp hoặc kéo thả ở đây
            </label>
          </div>
        );

      case 'checkbox':
        return (
          <input
            type="checkbox"
            id={id}
            checked={value || false}
            onChange={(e) => onChange(id, e.target.checked)}
            className="form-checkbox"
          />
        );

      case 'date':
        return (
          <input
            type="date"
            id={id}
            value={value || ''}
            onChange={(e) => onChange(id, e.target.value)}
            className={`form-input ${error ? 'error' : ''}`}
          />
        );

      default:
        return <input type="text" />; // Fallback
    }
  };

  return (
    <div className={`form-field ${error ? 'field-error' : ''}`}>
      <label htmlFor={id} className="field-label">
        {label}
        {required && <span className="required">*</span>}
      </label>
      {renderField()}
      {error && <div className="error-text">{error}</div>}
      {helperText && <div className="helper-text">{helperText}</div>}
    </div>
  );
};
```

---

## 🎨 Styling Guidelines

### Color Scheme
```css
/* Primary Colors */
--primary: #1976D2;      /* Blue */
--primary-light: #E3F2FD; /* Light Blue */
--primary-dark: #1565C0;  /* Dark Blue */

/* Semantic Colors */
--success: #4CAF50;   /* Green - for success/approved */
--warning: #FF9800;   /* Orange - for warnings */
--error: #F44336;     /* Red - for errors */
--info: #2196F3;      /* Blue - for info messages */

/* Neutral */
--bg-primary: #FFFFFF;    /* White background */
--bg-secondary: #F5F5F5;  /* Light gray background */
--text-primary: #333333;  /* Dark text */
--text-secondary: #666666; /* Gray text */
--border: #E0E0E0;        /* Border color */

/* Chat */
--user-message-bg: #1976D2;      /* Blue for user messages */
--user-message-text: #FFFFFF;    /* White text in user messages */
--consultant-message-bg: #F5F5F5; /* Gray for consultant messages */
--consultant-message-text: #333333;
```

### Typography
```css
/* Font Family */
font-family: 'Inter', 'Roboto', 'Segoe UI', sans-serif;

/* Font Sizes */
--h1: 32px (bold) - Page titles
--h2: 24px (bold) - Section titles
--h3: 20px (semibold) - Subsections
--h4: 16px (semibold) - Card titles
--body: 14px (regular) - Body text
--small: 12px (regular) - Helper/meta text
--tiny: 10px (regular) - Labels

/* Line Heights */
--lh-tight: 1.2
--lh-normal: 1.5
--lh-loose: 1.8
```

### Spacing System
```css
/* Base unit: 4px */
--space-xs: 4px
--space-sm: 8px
--space-md: 12px
--space-lg: 16px
--space-xl: 24px
--space-2xl: 32px
--space-3xl: 48px
```

### Component Styling Examples

**Button Styles:**
```css
.btn-primary {
  background-color: var(--primary);
  color: white;
  padding: var(--space-md) var(--space-lg);
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-weight: 600;
}

.btn-primary:hover {
  background-color: var(--primary-dark);
}

.btn-secondary {
  background-color: transparent;
  color: var(--primary);
  border: 1px solid var(--primary);
  padding: var(--space-md) var(--space-lg);
  border-radius: 4px;
}
```

**Card Styles:**
```css
.card {
  background: white;
  border-radius: 8px;
  border: 1px solid var(--border);
  padding: var(--space-lg);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}
```

**Input Styles:**
```css
.form-input {
  border: 1px solid var(--border);
  border-radius: 4px;
  padding: var(--space-md);
  font-size: 14px;
  font-family: inherit;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px var(--primary-light);
}

.form-input.error {
  border-color: var(--error);
  box-shadow: 0 0 0 3px rgba(244, 67, 54, 0.1);
}
```

---

## 🔄 State Management

### Recommended Approach
Use **Context API + Hooks** (lightweight) or **Redux** (if complex)

### State Structure
```javascript
// App-level state
const AppState = {
  // User
  user: {
    id: "user123",
    name: "Nguyễn Văn A",
    email: "user@example.com",
    avatar: "https://...",
    phone: "+84123456789"
  },

  // Chat/Consultation
  currentChat: {
    chatRoomId: "room123",
    consultantId: "consultant456",
    consultantName: "Lê Thị B",
    messages: [],
    status: "ACTIVE", // ACTIVE, CLOSED
    unreadCount: 2
  },

  // Consultants
  consultants: {
    list: [],
    selectedId: null,
    loading: false,
    error: null
  },

  // Forms
  forms: {
    currentFormId: "permit_chemical",
    fields: {},
    values: {},
    errors: {},
    currentStep: 1,
    totalSteps: 4
  },

  // UI
  ui: {
    showConsultationOffer: false,
    showRatingPanel: false,
    sidebarOpen: true,
    loading: false
  }
};
```

### Context Hooks
```javascript
// useAuth.js
export const useAuth = () => {
  const { user, login, logout } = useContext(AuthContext);
  return { user, login, logout };
};

// useChat.js
export const useChat = () => {
  const { messages, sendMessage, closeChat } = useContext(ChatContext);
  return { messages, sendMessage, closeChat };
};

// useForms.js
export const useForms = () => {
  const { forms, selectForm, updateField } = useContext(FormContext);
  return { forms, selectForm, updateField };
};
```

---

## ✅ Testing Checklist

### Feature 1: RAG Chat
- [ ] User can type and submit question
- [ ] AI response displays correctly
- [ ] Question history persists
- [ ] Consultation offer appears after 30 seconds
- [ ] User can decline offer
- [ ] Loading states show correctly

### Feature 2: Consultant System
- [ ] Top 5 consultants display correctly
- [ ] Consultant cards show all info (name, rating, specializations)
- [ ] Match score displays and is accurate
- [ ] User can click consultant card
- [ ] Chat room opens after consultant selection

### Feature 3: Chat & Rating
- [ ] Messages send and receive in real-time
- [ ] Message history loads on room open
- [ ] User can close chat and see rating panel
- [ ] Rating submission works (1-5 stars + optional feedback)
- [ ] Chat room closes after rating

### Feature 4: Forms
- [ ] Form fields auto-fill from user profile
- [ ] Form validation works
- [ ] File uploads work
- [ ] Multi-step navigation works
- [ ] Form submission succeeds

### Feature 5: Admin (if applicable)
- [ ] Excel template downloads correctly
- [ ] File upload accepts .xlsx
- [ ] Dry-run mode works (preview without save)
- [ ] Actual import saves data
- [ ] Error messages display clearly

### Responsive Design
- [ ] Mobile: < 768px
- [ ] Tablet: 768px - 1024px
- [ ] Desktop: > 1024px
- [ ] Touch interactions work on mobile

### Performance
- [ ] Initial page load < 3 seconds
- [ ] Chat message send < 1 second
- [ ] Form submission < 2 seconds
- [ ] No console errors

---

## 📚 Additional Resources

### API Documentation
See: `CONSULTANT_API_DOCUMENTATION.md`

### Backend Setup
See: `CONSULTANT_IMPLEMENTATION_GUIDE.md`

### Deployment
See: `DEPLOYMENT_GUIDE.md`

### Troubleshooting
See: `TROUBLESHOOTING.md`

---

## 🎯 Getting Started

### 1. Setup Environment
```bash
npm install
cp .env.example .env
# Update .env with API_BASE_URL and other config
```

### 2. Start Development
```bash
npm start
# Opens http://localhost:3000
```

### 3. Build for Production
```bash
npm run build
# Creates /build folder
```

### 4. Test API Endpoints
```bash
# Use test scripts provided:
# test-api.ps1 (Windows)
# test-api.sh (Mac/Linux)
```

---

## 💬 Questions?

Contact the backend team for:
- API endpoint clarifications
- Data format questions
- Integration issues
- Performance questions

---

**Last Updated: May 20, 2026**  
**Version: 1.0 - Complete & Ready for Implementation**

