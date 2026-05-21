# 🚀 QUICK TEST GUIDE - RUN NOW!

## 3 Ways to Test (Pick one!)

---

## ✅ **EASIEST: Run PowerShell Script**

### Step 1: Open PowerShell
- Press **Windows Key**
- Type: `powershell`
- Press Enter

### Step 2: Run Test Script
```powershell
cd D:\legal-rag-api-main\legal-rag-api-main
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
.\test-consultant-import-api.ps1 -Mode preview
```

### Step 3: Watch Results
- Should show JSON response with:
  ```
  "successCount": 5
  "errorCount": 0
  "success": true
  ```
- Status: **SUCCESS ✅**

---

## ✅ **VISUAL: Use Swagger UI**

### Step 1: Start Server (if not running)
```
http://localhost:8085/swagger-ui.html
```

### Step 2: Find Endpoint
- Look for: **POST /api/consultants/import**

### Step 3: Click "Try it out"

### Step 4: Upload File
- **dryRun**: Change to `true` (preview)
- **file**: Click "Choose Files" → Select:
  ```
  D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx
  ```

### Step 5: Click "Execute" (blue button)

### Step 6: See Response
```json
{
  "success": true,
  "totalRows": 5,
  "successCount": 5,
  "errorCount": 0,
  "message": "Import hoàn tất: 5 thành công, 0 lỗi"
}
```

---

## ✅ **ADVANCED: Use curl**

### Step 1: Open PowerShell/cmd

### Step 2: Run Command
```powershell
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" `
  -F "file=@D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx"
```

### Step 3: See Response
```json
{
  "success": true,
  "successCount": 5,
  ...
}
```

---

## 📋 Test Sequence (Recommended)

### Test 1: Check Server is Running
```powershell
.\test-consultant-import-api.ps1 -Mode template
```
✅ Should show template specifications

### Test 2: Preview Import (Dry Run)
```powershell
.\test-consultant-import-api.ps1 -Mode preview
```
✅ Should show: 5 successful, 0 errors

### Test 3: Actual Import
```powershell
.\test-consultant-import-api.ps1 -Mode import
```
✅ Will ask for confirmation, then save to MongoDB

---

## 🎯 What Gets Tested

```
✅ File upload endpoint
✅ Excel parsing (16 columns, Vietnamese headers)
✅ Data validation
✅ 5 sample consultants:
   - Nguyễn Văn Tư
   - Trần Thị Tư Vấn
   - Lê Văn Bình
   - Phạm Thị Dung
   - Đỗ Văn Minh
✅ Mock import (dryRun=true)
✅ Real import (dryRun=false)
```

---

## 📊 Expected Results

### Success Response
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

### HTTP Status: 200 ✅

---

## 🆘 Troubleshooting

### Problem: "Connection refused"
**Reason**: Server not running  
**Solution**: 
```bash
cd D:\legal-rag-api-main\legal-rag-api-main
mvn spring-boot:run
# Wait for "Started RagApiApplication"
```

### Problem: "File not found"
**Reason**: Wrong file path  
**Solution**:
```powershell
dir D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx
# Should show the file
```

### Problem: "Not .xlsx format"
**Reason**: Using CSV instead of Excel  
**Solution**: Use this file:
```
D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx (10.23 KB)
```

---

## 📁 Files You Have

| File | Size | Purpose |
|------|------|---------|
| consultant-import-template.xlsx | 10.23 KB | ✅ Excel file for testing |
| test-consultant-import-api.ps1 | ~4 KB | ✅ PowerShell test script |
| TEST_API_NOW.md | Reference | Detailed test instructions |

---

## ⚡ Quick Commands

**Test Template**:
```powershell
.\test-consultant-import-api.ps1 -Mode template
```

**Preview Import**:
```powershell
.\test-consultant-import-api.ps1 -Mode preview
```

**Actual Import**:
```powershell
.\test-consultant-import-api.ps1 -Mode import
```

---

**Status**: ✅ Ready to test  
**Time**: 30 seconds to run first test  

**Go ahead, test it now!** 🚀

