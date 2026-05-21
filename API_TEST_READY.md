# 📊 API TEST COMPLETE SETUP - Everything Ready!

**Date**: May 20, 2026  
**Status**: ✅ READY FOR TESTING  

---

## 🎯 What's Ready to Test

### ✅ API Endpoint
```
POST /api/consultants/import?dryRun=true/false
```
- Accepts: Excel file (.xlsx)
- Returns: JSON with import results
- Vietnamese messages: ✅

### ✅ Excel File
```
File: consultant-import-template.xlsx
Size: 10.23 KB
Location: D:\legal-rag-api-main\legal-rag-api-main\
Data: 5 sample consultants (Vietnamese names)
```

### ✅ Test Scripts
```
1. PowerShell: test-consultant-import-api.ps1
2. Swagger UI: http://localhost:8085/swagger-ui.html
3. curl: Command line direct test
```

### ✅ Documentation
```
1. QUICK_TEST_NOW.md - Complete test guide
2. TEST_API_NOW.md - Detailed test methods
3. SWAGGER_FILE_UPLOAD_FIXED.md - API documentation
```

---

## 🚀 Test Now (3 Options)

### QUICKEST: Run PowerShell
```powershell
cd D:\legal-rag-api-main\legal-rag-api-main
.\test-consultant-import-api.ps1 -Mode preview
```
✅ Result in 5 seconds: SUCCESS or ERROR message

---

### VISUAL: Swagger UI
```
http://localhost:8085/swagger-ui.html
→ POST /api/consultants/import
→ Try it out
→ Choose file: consultant-import-template.xlsx
→ Execute
```
✅ See response in browser

---

### MANUAL: curl Command
```bash
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
  -F "file=@D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx"
```
✅ Response printed to console

---

## 📋 Test Data Included

```
Consultant 1: Nguyễn Văn Tư
  - Code: CONS-001
  - Email: nguyen@consultant.com
  - Specializations: nhập khẩu hóa chất; xuất khẩu
  - Rating: 4.8/5
  - Experience: 15 years

Consultant 2: Trần Thị Tư Vấn
  - Code: CONS-002
  - Specializations: hóa chất; giấy phép môi trường
  - Rating: 4.5/5

Consultant 3: Lê Văn Bình
  - Code: CONS-003
  - Specializations: giấy phép; pháp luật môi trường
  - Rating: 4.7/5

Consultant 4: Phạm Thị Dung
  - Code: CONS-004
  - Specializations: nhập khẩu; tuân thủ quy định
  - Rating: 4.3/5

Consultant 5: Đỗ Văn Minh
  - Code: CONS-005  
  - Specializations: hóa chất nguy hại; xử lý chất thải
  - Rating: 4.9/5
  - Experience: 18 years
```

---

## 🎬 Step-by-Step (For Dummies)

### Step 1: Ensure Server is Running
```
http://localhost:8085  → Should load
```
If not:
```bash
cd D:\legal-rag-api-main\legal-rag-api-main
mvn spring-boot:run
# Wait for: "Started RagApiApplication in XX seconds"
```

### Step 2: Open PowerShell
```
Windows Key → "powershell" → Enter
```

### Step 3: Run Test
```powershell
cd D:\legal-rag-api-main\legal-rag-api-main
.\test-consultant-import-api.ps1 -Mode preview
```

### Step 4: Read Response
```
SUCCESS ✅ = Everything works!
ERROR ❌ = Check error message
```

---

## 📊 Expected Success Response

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

**HTTP Status**: 200 ✅

---

## 🔄 Three Test Modes

### Mode 1: Preview (dryRun=true)
- **Purpose**: Check if import would succeed
- **Data**: NOT saved to database
- **Use Case**: Test before actual import
- **Command**: 
  ```powershell
  .\test-consultant-import-api.ps1 -Mode preview
  ```

### Mode 2: Actual Import (dryRun=false)  
- **Purpose**: Save consultants to MongoDB
- **Data**: Saved permanently
- **Use Case**: Production import
- **Command**:
  ```powershell
  .\test-consultant-import-api.ps1 -Mode import
  ```
- **Note**: Requires confirmation (Y/N)

### Mode 3: Get Template (template)
- **Purpose**: Check column specifications
- **Data**: None
- **Use Case**: Verify API is working
- **Command**:
  ```powershell
  .\test-consultant-import-api.ps1 -Mode template
  ```

---

## ✨ Files Provided for Testing

| File | Type | Size | Purpose |
|------|------|------|---------|
| **consultant-import-template.xlsx** | Excel | 10.23 KB | Test data (5 consultants) |
| **test-consultant-import-api.ps1** | PowerShell | 4 KB | Automated test script |
| **QUICK_TEST_NOW.md** | Guide | Reference | Quick start guide |
| **TEST_API_NOW.md** | Guide | Reference | Detailed test methods |

---

## 🎯 Common Test Scenarios

### Scenario 1: "Does the API work?"
```powershell
.\test-consultant-import-api.ps1 -Mode template
```
✅ If it returns JSON → API works

### Scenario 2: "Can I upload Excel?"
```powershell
.\test-consultant-import-api.ps1 -Mode preview
```
✅ If it returns 5 successful → Works perfectly

### Scenario 3: "Save consultant data"
```powershell
.\test-consultant-import-api.ps1 -Mode import
```
✅ If success response → Data in MongoDB

### Scenario 4: "Manual test"
Use Swagger UI at `/swagger-ui.html`  
✅ Visual testing with "Try it out" button

---

## 🆘 If Something Goes Wrong

| Error | Cause | Solution |
|-------|-------|----------|
| Connection refused | Server not running | `mvn spring-boot:run` |
| File not found | Wrong path | Check file exists in location |
| Format not supported | Using CSV not XLSX | Use: consultant-import-template.xlsx |
| Validation error | Data format invalid | Check Excel column headers |
| MongoDB error | DB not connected | Ensure MongoDB running |

---

## ✅ Verification Checklist

- [ ] Excel file exists: `consultant-import-template.xlsx`
- [ ] Server running: http://localhost:8085
- [ ] Test script exists: `test-consultant-import-api.ps1`
- [ ] PowerShell ready: Can type `powershell`
- [ ] Documentation ready: All .md files available

---

## 🚀 Ready to Test?

**Pick ONE method:**

1. **PowerShell** (Easiest)
   ```powershell
   .\test-consultant-import-api.ps1 -Mode preview
   ```

2. **Swagger UI** (Visual)
   ```
   http://localhost:8085/swagger-ui.html
   ```

3. **curl** (Command)
   ```bash
   curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
     -F "file=@consultant-import-template.xlsx"
   ```

---

**Status**: ✅ COMPLETE & READY  
**Time to test**: 30 seconds  
**Expected success rate**: 100%  

**Test it now!** 🎉

