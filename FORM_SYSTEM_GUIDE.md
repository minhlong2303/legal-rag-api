# 📋 Hệ Thống Tự Động Phân Loại & Điền Form Hóa Chất

## 📌 Tổng Quan

Hệ thống này cho phép người dùng:
1. **Hỏi câu hỏi bằng ngôn ngữ tự nhiên** (vd: "Tôi muốn nhập khẩu hóa chất cấm, cần những giấy phép gì?")
2. **AI tự động tìm form phù hợp** dựa trên file .md được cung cấp
3. **Đưa ra danh sách form được suggest** (max 5 form có độ match cao nhất)
4. **Người dùng chọn form** mà họ muốn
5. **AI tự động điền form** dựa vào thông tin trong user profile
6. **Người dùng chỉnh sửa/hoàn thành form** và gửi

---

## 🔄 Luồng Xử Lý Chi Tiết

### Flow 1: Người Dùng Hỏi Câu Hỏi

```
User Input
    ↓
POST /api/forms/search
    ↓
FormSearchRequest
    {
        "question": "Tôi cần nhập khẩu hóa chất cấm, phải chuẩn bị giấy tờ gì?",
        "userId": "user123",
        "purpose": "import"  // optional
    }
    ↓
FormMatcherService.matchQuestionToForms()
    ↓
    1. Normalize câu hỏi (lowercase, xóa dấu, v.v.)
    2. Tokenize câu hỏi → ["nhập", "khẩu", "hóa", "chất", "cấm", ...]
    3. Load tất cả active forms từ MongoDB
    4. Score mỗi form dựa trên:
       - Keyword matching (mỗi match = 30 points)
       - Category matching (30 points)
       - Form type matching (20 points)
    5. Sort theo score (descending)
    6. Return top 5 forms
    ↓
FormSearchResponse
    {
        "question": "Tôi cần nhập khẩu hóa chất cấm...",
        "suggestions": [
            {
                "formId": "form-02a-123",
                "formCode": "02a",
                "formName": "Văn bản đề nghị cấp Giấy phép nhập khẩu hóa chất cấm",
                "description": "...",
                "matchScore": 95,
                "matchReason": "Khớp với từ khóa: nhập khẩu, hóa chất, cấm"
            },
            {
                "formId": "form-02b-456",
                "formCode": "02b",
                "formName": "Văn bản đề nghị cấp lại...",
                "matchScore": 60,
                "matchReason": "Khớp với từ khóa: hóa chất, cấm"
            },
            ... (tối đa 5 form)
        ],
        "explanation": "Tìm thấy 5 form phù hợp..."
    }
    ↓
UI Display Form List
```

**Keyword Matching Logic:**
- Input question được tokenize thành các từ
- So sánh từng token với keywords của form
- Nếu similarity score > 0.7 → match (dùng Levenshtein distance)

**Example:**
- Question: "nhập khẩu hóa chất cấm"
- Form 02a keywords: ["nhập khẩu", "hóa chất cấm", "cấp phép", "import"]
  - "nhập" → "nhập khẩu" (match) = 30 points
  - "khẩu" → "nhập khẩu" (match) = 30 points
  - "hóa" → "hóa chất cấm" (match) = 30 points
  - "chất" → "hóa chất cấm" (match) = 30 points
  - "cấm" → "hóa chất cấm" (match) = 30 points
  - **Total: 150 points** (normalized to 95/100)

---

### Flow 2: Người Dùng Chọn Form

```
User Clicks "Chọn Form"
    ↓
POST /api/forms/detail
    ↓
FormDetailRequest
    {
        "formId": "form-02a-123",
        "userId": "user123",
        "autoFill": true
    }
    ↓
FormAutoFillService.getFormDetailWithAutoFill()
    ↓
    1. Lấy form template từ MongoDB
    2. Lấy user profile từ MongoDB (nếu userId có)
    3. Duyệt qua tất cả fields trong form
    4. Với mỗi field:
       a. Check autoFillSource (vd: "user.organizationName")
       b. Nếu có giá trị trong user profile → thêm vào filledData
       c. Nếu không có giá trị → thêm vào emptyFields
    5. Generate HTML template với:
       - Filled fields: hiển thị giá trị + readonly hoặc editable
       - Empty fields: hiển thị form input trống để người dùng điền
    ↓
FormDetailResponse
    {
        "form": {
            "id": "form-02a-123",
            "formCode": "02a",
            "formName": "Văn bản đề nghị cấp Giấy phép nhập khẩu...",
            "description": "...",
            "formType": "IMPORT_CHEMICAL_PERMIT"
        },
        "filledData": {
            "organizationName": "ABC Company Ltd",
            "organizationAddress": "123 Đường A, Quận B, TP.HCM",
            "organizationPhone": "0123456789",
            "legalRepresentativeName": "Nguyễn Văn A"
        },
        "emptyFields": {
            "chemicalName": {
                "fieldId": "chemicalName",
                "displayLabel": "Tên hoá chất",
                "fieldType": "TEXT",
                "placeholder": "VD: Insecticide ABC",
                "required": true,
                "suggestions": [...],
                "notes": "Nhập tên thương mại của hoá chất"
            },
            "chemicalCAS": {
                "fieldId": "chemicalCAS",
                "displayLabel": "Mã CAS",
                "fieldType": "TEXT",
                "placeholder": "VD: 123-45-6",
                "required": true,
                "notes": "Mã CAS từ IUPAC"
            },
            ... (các field trống khác)
        },
        "formTemplate": "<form class=\"chemical-form\">\n<h2>Văn bản đề nghị...</h2>\n<div class=\"form-group\">\n<label for=\"organizationName\">Tên tổ chức</label>\n<input type=\"text\" id=\"organizationName\" name=\"organizationName\" value=\"ABC Company Ltd\" readonly>\n</div>\n<div class=\"form-group required\">\n<label for=\"chemicalName\">Tên hoá chất</label>\n<input type=\"text\" id=\"chemicalName\" name=\"chemicalName\" placeholder=\"VD: Insecticide ABC\" required>\n</div>\n...</form>"
    }
    ↓
UI Render Form
    - Filled fields: disabled hoặc readonly (người dùng có thể edit)
    - Empty fields: input trống để người dùng điền
    - Alternative: Show form in 2 states:
      1. "Đã điền tự động" (tên, địa chỉ, v.v.)
      2. "Cần điền" (hoá chất, khối lượng, v.v.)
```

**Auto-Fill Source Mapping:**
```
Form Field → User Profile Field
organizationName → user.organizationName
organizationAddress → user.organizationAddress
organizationPhone → user.organizationPhone
warehouseAddress → user.warehouseAddress
legalRepresentativeName → user.legalRepresentativeName
customsPort → user.customsPort
```

---

### Flow 3: Người Dùng Submit Form

```
User Fills & Clicks "Gửi"
    ↓
POST /api/forms/submit
    ↓
FormSubmitRequest
    {
        "formId": "form-02a-123",
        "userId": "user123",
        "formData": {
            "organizationName": "ABC Company Ltd",
            "organizationAddress": "123 Đường A...",
            "chemicalName": "Insecticide ABC",
            "chemicalCAS": "123-45-6",
            "quantity": "1000",
            ...
        }
    }
    ↓
Validation
    ↓
Save Submitted Form to MongoDB
    ↓
Update User Profile (nếu cần)
    ↓
FormSubmitResponse
    {
        "status": "SUCCESS",
        "message": "Form đã được gửi thành công!",
        "submittedFormId": "submitted-form-123",
        "nextSteps": "Vui lòng kiểm tra email để nhận thông tin tiếp theo"
    }
```

---

## 📊 Database Schema

### Collection: form_templates
```json
{
  "_id": "form-02a-123",
  "formCode": "02a",
  "formName": "Văn bản đề nghị cấp Giấy phép nhập khẩu hóa chất cấm",
  "formType": "IMPORT_CHEMICAL_PERMIT",
  "description": "...",
  "keywords": ["nhập khẩu", "hóa chất cấm", "cấp phép", "import", "chemical"],
  "categories": ["import", "chemical", "permit"],
  "fields": [
    {
      "fieldId": "organizationName",
      "fieldName": "organizationName",
      "displayLabel": "Tên tổ chức",
      "fieldType": "TEXT",
      "placeholder": "Nhập tên đơn vị",
      "required": true,
      "validationRules": ["required", "minLength:3"],
      "autoFillSource": "user.organizationName",
      "notes": "Tên tổ chức đăng ký cấp phép"
    },
    {
      "fieldId": "chemicalName",
      "fieldName": "chemicalName",
      "displayLabel": "Tên hoá chất",
      "fieldType": "TEXT",
      "placeholder": "VD: Insecticide ABC",
      "required": true,
      "validationRules": ["required"],
      "autoFillSource": null,  // Không auto-fill
      "notes": "Nhập tên thương mại"
    },
    ... (nhiều fields khác)
  ],
  "placeholderMarkdown": "...",
  "createdAt": "2026-05-16T10:00:00",
  "updatedAt": "2026-05-16T10:00:00",
  "active": true
}
```

### Collection: user_profiles
```json
{
  "_id": "profile-user123",
  "userId": "user123",
  "email": "user@example.com",
  "organizationName": "ABC Company Ltd",
  "organizationAddress": "123 Đường A, Quận B, TP.HCM",
  "organizationPhone": "0123456789",
  "organizationEmail": "info@abc.com",
  "businessRegistrationNumber": "BRN-123456",
  "investmentCertificateNumber": "ICN-789012",
  "organizationId": "ORG-123",
  "warehouseAddress": "456 Đường C, Quận D, TP.HCM",
  "warehousePhone": "0987654321",
  "productionAddresses": ["789 Đường E, Quận F"],
  "legalRepresentativeName": "Nguyễn Văn A",
  "legalRepresentativePosition": "Giám đốc",
  "authorizedPersonName": "Trần Thị B",
  "authorizedPersonPhone": "0123456788",
  "authorizedPersonEmail": "tran@abc.com",
  "customsPort": "Tân Sơn Nhất",
  "customsProcedureLocation": "Chi cục Hải quan Tân Sơn Nhất",
  "additionalInfo": {},
  "createdAt": "2026-05-16T10:00:00",
  "updatedAt": "2026-05-16T10:00:00",
  "completionStatus": true
}
```

### Collection: submitted_forms
```json
{
  "_id": "submitted-form-123",
  "formId": "form-02a-123",
  "userId": "user123",
  "submittedData": {
    "organizationName": "ABC Company Ltd",
    "organizationAddress": "123 Đường A...",
    ...
  },
  "submissionDate": "2026-05-16T10:30:00",
  "status": "SUBMITTED",
  "validationErrors": {},
  "notes": ""
}
```

---

## 🔧 API Endpoints

### 1. Search Forms (Tìm Form)
```
POST /api/forms/search
Content-Type: application/json

{
  "question": "Tôi muốn nhập khẩu hóa chất cấm",
  "userId": "user123",
  "purpose": "import"
}

Response:
{
  "question": "Tôi muốn nhập khẩu hóa chất cấm",
  "suggestions": [
    {
      "formId": "form-02a-123",
      "formCode": "02a",
      "formName": "Văn bản đề nghị cấp Giấy phép nhập khẩu hóa chất cấm",
      "description": "...",
      "matchScore": 95,
      "matchReason": "Khớp với từ khóa: nhập khẩu, hóa chất, cấm"
    }
  ],
  "explanation": "Tìm thấy 5 form phù hợp..."
}
```

### 2. Get Form Detail with Auto-Fill (Lấy Form & Auto-Fill)
```
POST /api/forms/detail
Content-Type: application/json

{
  "formId": "form-02a-123",
  "userId": "user123",
  "autoFill": true
}

Response:
{
  "form": { ... },
  "filledData": { ... },
  "emptyFields": { ... },
  "formTemplate": "<form>...</form>"
}
```

### 3. Submit Form (Gửi Form)
```
POST /api/forms/submit
Content-Type: application/json

{
  "formId": "form-02a-123",
  "userId": "user123",
  "formData": { ... }
}

Response:
{
  "status": "SUCCESS",
  "message": "Form đã được gửi thành công!",
  "submittedFormId": "submitted-form-123",
  "nextSteps": "Vui lòng kiểm tra email..."
}
```

### 4. Update User Profile (Cập Nhật Profile)
```
POST /api/forms/profile/update?userId=user123
Content-Type: application/json

{
  "organizationName": "ABC Company Ltd",
  "organizationAddress": "123 Đường A, TP.HCM",
  "organizationPhone": "0123456789"
}

Response:
{
  "status": "SUCCESS",
  "message": "Profile đã được cập nhật thành công!"
}
```

---

## 🎯 Keyword Matching Algorithm

**Similarity Calculation (Levenshtein Distance):**

```
Input: "nhập khẩu hóa chất cấm"
Form Keywords: ["nhập khẩu", "hóa chất cấm", "cấp phép"]

Step 1: Tokenize
tokens = ["nhập", "khẩu", "hóa", "chất", "cấm"]

Step 2: Calculate Similarity for each token
- "nhập" vs "nhập khẩu" → distance=4, similarity=0.8 → MATCH (>0.7)
- "khẩu" vs "nhập khẩu" → distance=4, similarity=0.8 → MATCH
- "hóa" vs "hóa chất cấm" → distance=6, similarity=0.75 → MATCH
- "chất" vs "hóa chất cấm" → distance=5, similarity=0.8 → MATCH
- "cấm" vs "hóa chất cấm" → distance=5, similarity=0.8 → MATCH

Step 3: Calculate Score
matches = 5
score = matches * 30 = 150
normalized_score = min(150, 100) = 100

But we also consider other forms' scores relatively, so final normalized score is 95-100
```

---

## 🛡️ Error Handling

```
Case 1: Không tìm thấy form
- Return FormSearchResponse với suggestions = []
- Explanation chứa gợi ý thử lại với cách diễn đạt khác

Case 2: Form không tồn tại
- Return 404 error
- Message: "Form không tồn tại"

Case 3: User profile không tồn tại
- Auto-fill với empty values
- Message: "Cập nhật profile để auto-fill nhanh hơn lần sau"

Case 4: Validation error
- Return FormSubmitResponse với status="VALIDATION_ERROR"
- Liệt kê tất cả validation errors
```

---

## 📝 Implementation Checklist

- [ ] Create FormTemplate entity
- [ ] Create UserProfile entity
- [ ] Create FormDTOs (SearchRequest, SearchResponse, DetailRequest, DetailResponse, SubmitRequest, SubmitResponse)
- [ ] Create FormTemplateRepository
- [ ] Create UserProfileRepository
- [ ] Create FormMatcherService (keyword matching)
- [ ] Create FormAutoFillService (auto-fill logic)
- [ ] Create FormController with REST endpoints
- [ ] Parse file .md để populate form_templates collection
- [ ] Create UI/Frontend để display forms
- [ ] Write tests
- [ ] Deploy

---

## 🚀 Next Steps

1. **Parse File .md**: Tạo service để parse file .md này → extract forms + keywords
2. **Populate Database**: Load forms vào MongoDB
3. **Test Matching**: Test keyword matching với các câu hỏi khác nhau
4. **Frontend**: Tạo UI để hiển thị form suggestions
5. **Integration**: Integrate với existing RAG API

---

**Status**: Ready for implementation ✅

