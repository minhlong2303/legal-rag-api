# 🧪 TEST CONSULTANT IMPORT API

**File Excel Ready**: `consultant-import-template.xlsx` (10.23 KB)

---

## ✅ Option 1: Test Bằng curl (Easiest)

### Step 1: Đảm bảo Server Đang Chạy
```bash
# Check if server is running
curl http://localhost:8085/api/consultants/import/template
```

Nếu trả về JSON → Server running ✅  
Nếu lỗi → Cần start server trước

### Step 2: Upload Excel File (Preview Mode)

```bash
# Test mode (dryRun=true) - không lưu vào DB
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
  -F "file=@D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx"
```

**Expected Response** (nếu mọi thứ OK):
```json
{
  "success": true,
  "message": "Import hoàn tất: 5 thành công, 0 lỗi, 5 tổng cộng",
  "totalRows": 5,
  "successCount": 5,
  "errorCount": 0,
  "dryRun": true,
  "successMessages": [
    "Row 2: Nguyễn Văn Tư - OK",
    "Row 3: Trần Thị Tư Vấn - OK",
    "Row 4: Lê Văn Bình - OK",
    "Row 5: Phạm Thị Dung - OK",
    "Row 6: Đỗ Văn Minh - OK"
  ],
  "errorMessages": []
}
```

### Step 3: Commit Import (Actual Save)

```bash
# Actual import (dryRun=false) - lưu vào DB
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=false" \
  -F "file=@D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx"
```

**Same response** nhưng `"dryRun": false` → Data saved to MongoDB ✅

---

## ✅ Option 2: Test Bằng Swagger UI (Visual)

### Step 1: Mở Swagger UI
```
http://localhost:8085/swagger-ui.html
```

### Step 2: Tìm "POST /api/consultants/import"

### Step 3: Click "Try it out"

### Step 4: Set Parameters
```
dryRun: true (preview first)
```

### Step 5: Upload File
- Click **"Choose file"** button
- Select: `D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx`

### Step 6: Click **Execute** (blue button)

### Step 7: See Response (Success!)
```json
{
  "success": true,
  "message": "Import hoàn tất: 5 thành công, 0 lỗi",
  ...
}
```

---

## ✅ Option 3: Test Bằng Postman

### Step 1: Mở Postman

### Step 2: Tạo New Request
- **Method**: POST
- **URL**: `http://localhost:8085/api/consultants/import?dryRun=true`

### Step 3: Tab "Body"
- Select: **form-data**
- **Key**: `file`
- **Type**: **File**
- **Value**: Click "Choose Files" → Select Excel file

### Step 4: Send

### Step 5: Xem Response
```json
{
  "success": true,
  "totalRows": 5,
  "successCount": 5,
  ...
}
```

---

## 📍 File Locations

**Excel Template đã tạo:**
```
D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx
```

**Có thể dùng trong các command ở trên**

---

## 🔍 Troubleshooting

### Lỗi 1: "Connection refused"
```
❌ http://localhost:8085: Connection refused
```
**Fix**: Start server trước
```bash
cd D:\legal-rag-api-main\legal-rag-api-main
mvn spring-boot:run
```

### Lỗi 2: "File not found"  
```
❌ File does not exist at path
```
**Fix**: Kiểm tra đường dẫn Excel file
```bash
dir D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx
```

### Lỗi 3: "File format not supported"
```
❌ File not .xlsx or .xls
```
**Fix**: Dùng đúng file: `consultant-import-template.xlsx`

---

## ✅ Success Indicators

✅ Response status: 200 (not 400, 500)  
✅ "success": true  
✅ "successCount": 5  
✅ "errorCount": 0  
✅ All 5 consultants in successMessages  

---

## 📊 What Gets Imported

```
✅ CONS-001: Nguyễn Văn Tư (15 năm kinh nghiệm)
✅ CONS-002: Trần Thị Tư Vấn (10 năm kinh nghiệm)
✅ CONS-003: Lê Văn Bình (Chuyên gia pháp luật)
✅ CONS-004: Phạm Thị Dung (8 năm kinh nghiệm)
✅ CONS-005: Đỗ Văn Minh (18 năm chuyên gia)
```

Lưu vào MongoDB collection: `consultants`

---

## 🎯 Test Sequence

1. **Test GET** (Check template spec):
   ```bash
   curl http://localhost:8085/api/consultants/import/template
   ```

2. **Test POST (dryRun=true)** (Preview):
   ```bash
   curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
     -F "file=@consultant-import-template.xlsx"
   ```

3. **Test POST (dryRun=false)** (Actual import):
   ```bash
   curl -X POST "http://localhost:8085/api/consultants/import?dryRun=false" \
     -F "file=@consultant-import-template.xlsx"
   ```

---

**Ready to Test?** Pick one method above and let's go! 🚀

