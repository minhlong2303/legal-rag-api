# 📋 Consultant Import Guide - Hướng Dẫn Import Tài Khoản Tư Vấn

## 📌 Overview

Admin/Công ty có thể **bulk import tài khoản consultant** từ file Excel thay vì tạo từng cái một.

**Lợi ích**:
- ✅ Import hàng loạt (1000+ consultants cùng 1 lần)
- ✅ Tiết kiệm thời gian
- ✅ Validation tự động (kiểm tra lỗi trước import)
- ✅ Chi tiết báo cáo (success/error)

---

## 🌐 📚 Hướng Dẫn Chi Tiết (Tiếng Việt)

> **👉 Để có hướng dẫn Excel chi tiết 100% bằng Tiếng Việt, xem**: [EXCEL_TEMPLATE_GUIDE_VIETNAMESE.md](./EXCEL_TEMPLATE_GUIDE_VIETNAMESE.md)

Tài liệu này bao gồm:
- ✅ Header Tiếng Việt (16 cột)
- ✅ 5 ví dụ dữ liệu sẵn sàng
- ✅ CSV template có thể sao chép trực tiếp
- ✅ Các lỗi phổ biến & cách fix
- ✅ Cách tạo file Excel step-by-step

**File Template Có Sẵn:**
- `consultant-import-template.csv` - CSV template (sao chép vào Excel)
- `generate-excel-template.py` - Python script để generate Excel (nếu cần)

---

## 🎯 Format Excel - Dòng & Cột

### Excel Header (Dòng 1 - Header)

```
| A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P |
```

**Cột Chi Tiết** (Tiếng Việt):

| Cột | Tên Column - Tiếng Việt | Kiểu Dữ Liệu | Required | Ví Dụ |
|-----|-----------|------------|----------|-------|
| **A** | Mã Tư Vấn | Text | ✅ YES | CONS-001 |
| **B** | Tên Tư Vấn | Text | ✅ YES | Nguyễn Văn Tư |
| **C** | Email | Email | ✅ YES | nguyen@consultant.com |
| **D** | Điện Thoại | Text | ✅ YES | 0123456789 |
| **E** | Chuyên Môn | Text (phẩy ngăn cách) | ✅ YES | nhập khẩu hóa chất, xuất khẩu |
| **F** | Danh Mục | Text (phẩy ngăn cách) | ✅ YES | import, chemical, permit |
| **G** | Năm Kinh Nghiệm | Number | ❌ NO | 15 |
| **H** | Đánh Giá Trung Bình | Decimal (0-5) | ❌ NO | 4.8 |
| **I** | Số Tư Vấn Hoàn Thành | Number | ❌ NO | 150 |
| **J** | Thời Gian Phản Hồi (Phút) | Number | ❌ NO | 2 |
| **K** | Số Chat Tối Đa | Number | ❌ NO | 5 |
| **L** | Website | URL | ❌ NO | https://example.com |
| **M** | Địa Chỉ | Text | ❌ NO | 123 Đường A, Quận B |
| **N** | Thành Phố | Text | ❌ NO | TP.HCM |
| **O** | Mô Tả | Text (dài) | ❌ NO | 15+ năm kinh nghiệm... |
| **P** | Kích Hoạt | YES/NO hoặc 1/0 | ❌ NO | YES |

---

## 📝 Ví Dụ Dòng Dữ Liệu

### Dòng 2 (Row 2) - Consultant 1

```
| CONS-001 | Nguyễn Văn Tư | nguyen@consultant.com | 0123456789 | nhập khẩu hóa chất, xuất khẩu | import, chemical, permit | 15 | 4.8 | 150 | 2 | 5 | https://example.com | 123 Đường A, TP.HCM | TP.HCM | 15+ kinh nghiệm | YES |
```

### Dòng 3 (Row 3) - Consultant 2

```
| CONS-002 | Trần Thị Tư Vấn | tran@consultant.com | 0987654321 | hóa chất, giấy phép | chemical, permit | 10 | 4.5 | 120 | 3 | 4 | https://example.com | 456 Đường C, TP.HCM | TP.HCM | 10+ kinh nghiệm | YES |
```

---

## 📥 Cách Import

### Step 1: Chuẩn Bị File Excel

**Tải template** (nếu có):
```
GET /api/consultants/import/template
```

**Hoặc tạo file mới**:
1. Mở Excel
2. Tạo dòng header (dòng 1)
3. Nhập dữ liệu từ dòng 2 trở đi

---

### Step 2: Upload File

#### Option A: Preview Mode (`dryRun=true`)

**Mục đích**: Kiểm tra lỗi (nếu có) TRƯỚC import thực tế

```bash
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
  -F "file=@consultants.xlsx"
```

**Response** (nếu có lỗi):
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

#### Option B: Import Mode (`dryRun=false` hoặc bỏ qua)

**Mục đích**: Import thực tế vào database

```bash
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=false" \
  -F "file=@consultants.xlsx"
```

**Response** (nếu thành công):
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

## ⚠️ Validation Rules

### Required Fields (PHẢI CÓ)
- ✅ Consultant Code - không trùng lặp, không để trống
- ✅ Consultant Name - không để trống
- ✅ Email - format đúng (abc@example.com), không trùng lặp
- ✅ Phone - không để trống
- ✅ Specializations - ít nhất 1 chuyên môn
- ✅ Categories - ít nhất 1 danh mục

### Optional Fields (CÓ THỂTRỐNG)
- ❌ Experience Years - mặc định = 0
- ❌ Average Rating - mặc định = 0.0 (phải trong 0-5)
- ❌ Completed Consultations - mặc định = 0
- ❌ Response Time Minutes - mặc định = 5
- ❌ Max Concurrent Chats - mặc định = 5
- ❌ Website - không bắt buộc
- ❌ Address - không bắt buộc
- ❌ City - không bắt buộc
- ❌ Description - không bắt buộc
- ❌ Is Active - mặc định = YES (để trống = YES)

---

## 🔧 Format Chi Tiết - Mỗi Cột

### A: Consultant Code
```
Format: Text (không khoảng trắng lạ)
Ví dụ: CONS-001, CONS-002, ABC-2026-01
Yêu cầu: Không trùng lặp, không để trống
```

### B: Consultant Name
```
Format: Text
Ví dụ: Nguyễn Văn Tư, Trần Thị Tư Vấn
Yêu cầu: Không để trống
```

### C: Email
```
Format: Valid email format
Ví dụ: nguyen@consultant.com, tran@company.com.vn
Yêu cầu: Không trùng lặp, phải hợp lệ, không để trống
```

### D: Phone
```
Format: Text (số điện thoại)
Ví dụ: 0123456789, +84123456789, (123) 456-7890
Yêu cầu: Không để trống
```

### E & F: Specializations & Categories
```
Format: Comma-separated (ngăn cách bằng dấu phẩy)
Ví dụ: 
  - "nhập khẩu hóa chất, xuất khẩu, môi trường"
  - "import, export, chemical"
Yêu cầu: Ít nhất 1 item, không để trống
```

### G: Experience Years
```
Format: Number (số nguyên)
Ví dụ: 15, 10, 5
Yêu cầu: Không bắt buộc (mặc định = 0)
```

### H: Average Rating
```
Format: Decimal trong khoảng 0-5
Ví dụ: 4.8, 4.5, 3.0
Yêu cầu: Không bắt buộc (mặc định = 0.0, phải <= 5)
```

### I: Completed Consultations
```
Format: Number (số nguyên)
Ví dụ: 150, 120, 50
Yêu cầu: Không bắt buộc (mặc định = 0)
```

### J: Response Time Minutes
```
Format: Number (phút)
Ví dụ: 2, 3, 5
Yêu cầu: Không bắt buộc (mặc định = 5)
```

### K: Max Concurrent Chats
```
Format: Number (số chat tối đa)
Ví dụ: 5, 10, 3
Yêu cầu: Không bắt buộc (mặc định = 5)
```

### L: Website
```
Format: URL
Ví dụ: https://example.com, http://consultant.com.vn
Yêu cầu: Không bắt buộc
```

### M, N: Address & City
```
Format: Text
Ví dụ: 
  - Address: "123 Đường A, Quận B"
  - City: "TP.HCM", "Hà Nội"
Yêu cầu: Không bắt buộc
```

### O: Description
```
Format: Text (có thể dài)
Ví dụ: "15+ năm kinh nghiệm trong nhập khẩu hóa chất. Đã tư vấn cho 150+ công ty..."
Yêu cầu: Không bắt buộc
```

### P: Is Active
```
Format: YES/NO (hoặc true/false, 1/0)
Ví dụ: YES, NO, true, false, 1, 0
Yêu cầu: Không bắt buộc (mặc định = YES)
```

---

## 📋 Template Excel - Dòng Header

```
Dòng 1 (Header):
| Consultant Code | Consultant Name | Email | Phone | Specializations | Categories | Experience Years | Average Rating | Completed Consultations | Response Time Minutes | Max Concurrent Chats | Website | Address | City | Description | Is Active |
```

---

## 🛠️ Công Cụ Hỗ Trợ

### Generate Excel từ CSV
Nếu bạn có file CSV, convert sang Excel:
```bash
# Windows PowerShell
$csv = Import-Csv "consultants.csv"
$csv | Export-Excel -Path "consultants.xlsx" -AutoSize
```

### Generate từ Database Query
```sql
-- Export từ database cũ
SELECT 
  CONCAT('CONS-', ROW_NUMBER()) AS ConsultantCode,
  consultant_name,
  email,
  phone,
  STRING_AGG(specialization, ', '),
  STRING_AGG(category, ', '),
  ...
FROM old_database.consultants
```

---

## ✅ Checklist Pre-Import

- [ ] File format là .xlsx hoặc .xls
- [ ] Dòng 1 có header đầy đủ
- [ ] Tất cả required fields không trống
- [ ] Email format hợp lệ
- [ ] Email không trùng lặp
- [ ] Consultant Code không trùng lặp
- [ ] Rating nằm trong 0-5
- [ ] Specializations & Categories có ít nhất 1 item
- [ ] File size < 5MB
- [ ] Không có ký tự đặc biệt lạ

---

## 🚀 Import Process

```
User Upload File
    ↓
1. Validate File (extension, size)
    ↓
2. Parse Excel
    ↓
3. For each row:
   ├─ Parse consultant data
   ├─ Validate (required fields, format)
   ├─ Check duplicate (email, code)
   └─ Add to success/error list
    ↓
4. Return Report:
   ├─ Total rows
   ├─ Success count
   ├─ Error count
   ├─ Success messages
   └─ Error messages
    ↓
5. If dryRun=false:
   └─ Save all to database
```

---

## 📊 Ví Dụ Excel File Hoàn Chỉnh

**consultants.xlsx** (7 dòng):

```
Row 1 (Header):
Consultant Code | Consultant Name | Email | Phone | Specializations | Categories | Experience Years | Average Rating | Completed Consultations | Response Time Minutes | Max Concurrent Chats | Website | Address | City | Description | Is Active

Row 2:
CONS-001 | Nguyễn Văn Tư | nguyen@consultant.com | 0123456789 | nhập khẩu hóa chất, xuất khẩu | import, chemical | 15 | 4.8 | 150 | 2 | 5 | https://example.com | 123 Đường A | TP.HCM | 15+ kinh nghiệm | YES

Row 3:
CONS-002 | Trần Thị Tư Vấn | tran@consultant.com | 0987654321 | hóa chất, giấy phép | chemical, permit | 10 | 4.5 | 120 | 3 | 4 | | 456 Đường C | TP.HCM | 10+ kinh nghiệm | YES

Row 4:
CONS-003 | Lê Văn B | le@consultant.com | 0111111111 | môi trường, regulations | environmental | 8 | 4.2 | 80 | 4 | 3 | | | Hà Nội | 8+ kinh nghiệm | YES

Row 5:
CONS-004 | Phạm Thị D | pham@consultant.com | 0222222222 | hóa chất công nghiệp | industrial | 12 | 4.7 | 140 | 2 | 5 | | | Đà Nẵng | 12+ kinh nghiệm | YES

Row 6:
CONS-005 | Đỗ Văn E | do@consultant.com | 0333333333 | nhập khẩu, xuất khẩu, giấy phép | import, export, permit | 20 | 4.9 | 200 | 1 | 5 | https://do.com | 789 Đường E | TP.HCM | 20+ kinh nghiệm | YES

Row 7:
CONS-006 | Invalid Consultant | invalid-email | | | | | | | | | | | | | NO
(Dòng này sẽ lỗi - email invalid, phone trống, specializations trống)
```

---

## 🎯 API Endpoints

### 1. Download Template
```
GET /api/consultants/import/template
```

### 2. Preview (Dry Run)
```
POST /api/consultants/import?dryRun=true
Content-Type: multipart/form-data
- file: consultant-import.xlsx
```

### 3. Import Thực Tế
```
POST /api/consultants/import?dryRun=false
Content-Type: multipart/form-data
- file: consultant-import.xlsx
```

---

## 💡 Best Practices

1. **Luôn test với dryRun=true trước**
   - Kiểm tra lỗi
   - Fix Excel
   - Sau đó import với dryRun=false

2. **Giữ file Excel organize**
   - Column header rõ ràng
   - Dữ liệu sạch (không khoảng trắng lạ)
   - Email/Code không trùng

3. **Backup dữ liệu cũ**
   - Trước khi import bulk
   - Để có thể rollback nếu cần

4. **Test từng batch nhỏ**
   - Import 10-20 consultant đầu tiên
   - Verify trong database
   - Sau đó import phần còn lại

---

**Status**: ✅ Ready for Import
**Last Updated**: May 20, 2026


