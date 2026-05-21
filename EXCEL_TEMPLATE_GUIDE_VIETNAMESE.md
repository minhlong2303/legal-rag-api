# 📊 Excel Template - Hướng Dẫn Chi Tiết

**Tên File**: `consultant-import-template.xlsx`  
**Format**: Microsoft Excel (.xlsx)  
**Mã Hóa**: UTF-8 (hỗ trợ Tiếng Việt)  
**Số Cột**: 16 columns  
**Header**: Tiếng Việt  

---

## 📋 Dòng 1 - Header (Tiếng Việt)

Sao chép và dán 16 cột này vào dòng 1 của Excel:

```
Mã Tư Vấn  |  Tên Tư Vấn  |  Email  |  Điện Thoại  |  Chuyên Môn  |  Danh Mục  |  Năm Kinh Nghiệm  |  Đánh Giá TB (0-5)  |  Số Tư Vấn Hoàn Thành  |  Thời Gian Phản Hồi (Phút)  |  Số Chat Tối Đa  |  Website  |  Địa Chỉ  |  Thành Phố  |  Mô Tả  |  Kích Hoạt
```

**Hoặc trong Excel: Cột A → P**

| Cột | Tiêu Đề |
|-----|---------|
| A | Mã Tư Vấn |
| B | Tên Tư Vấn |
| C | Email |
| D | Điện Thoại |
| E | Chuyên Môn |
| F | Danh Mục |
| G | Năm Kinh Nghiệm |
| H | Đánh Giá TB (0-5) |
| I | Số Tư Vấn Hoàn Thành |
| J | Thời Gian Phản Hồi (Phút) |
| K | Số Chat Tối Đa |
| L | Website |
| M | Địa Chỉ |
| N | Thành Phố |
| O | Mô Tả |
| P | Kích Hoạt |

---

## 📝 Ví Dụ Dữ Liệu - 5 Chuyên Gia

### Dòng 2 (Consultant 1)
```
CONS-001 | Nguyễn Văn Tư | nguyen@consultant.com | 0123456789 | nhập khẩu hóa chất, xuất khẩu | import, chemical, permit | 15 | 4.8 | 150 | 2 | 5 | https://example.com | 123 Đường Lê Lợi, Quận 1 | TP.HCM | 15+ năm kinh nghiệm trong nhập khẩu hóa chất | YES
```

### Dòng 3 (Consultant 2)
```
CONS-002 | Trần Thị Tư Vấn | tran@consultant.com | 0987654321 | hóa chất, giấy phép môi trường | chemical, permit, environment | 10 | 4.5 | 120 | 3 | 4 | https://tran-consulting.vn | 456 Đường Nguyễn Huệ, Quận 1 | TP.HCM | 10+ năm tư vấn về quy định môi trường | YES
```

### Dòng 4 (Consultant 3)
```
CONS-003 | Lê Văn Bình | le.van.binh@consultant.vn | 0918765432 | giấy phép, pháp luật môi trường | permit, environment, legal | 12 | 4.7 | 98 | 5 | 3 | | 789 Đường D, Quận 2 | TP.HCM | Chuyên gia pháp luật môi trường | YES
```

### Dòng 5 (Consultant 4)
```
CONS-004 | Phạm Thị Dung | pham.dung@consultant.com | 0945123456 | nhập khẩu, tuân thủ quy định | import, compliance, chemical | 8 | 4.3 | 67 | 4 | 3 | https://phampham.com | 101 Đường Pasteur, Quận 3 | TP.HCM | 8 năm kinh nghiệm tư vấn | YES
```

### Dòng 6 (Consultant 5)
```
CONS-005 | Đỗ Văn Minh | do.minh@expert.vn | 0912345678 | hóa chất nguy hại, xử lý chất thải | chemical, waste, hazmat | 18 | 4.9 | 200 | 1 | 6 | https://dominh-experts.com | 999 Cách Mạng Tháng Tám, Quận 3 | TP.HCM | 18 năm chuyên gia | YES
```

---

## 🎨 Cách Tạo File Excel

### ⚠️ QUAN TRỌNG: CSV vs Excel

**File `consultant-import-template.csv`** là **TEXT format** (dữ liệu thô)

**Để upload API, cần chuyển sang Excel format (.xlsx)**

👉 **Xem chi tiết**: [CSV_TO_EXCEL_CONVERSION_GUIDE.md](./CSV_TO_EXCEL_CONVERSION_GUIDE.md)

---

### Cách 1: Dùng Microsoft Excel (30 giây) - EASIEST ⭐

1. **Mở CSV trong Excel**
   - Right-click `consultant-import-template.csv`
   - Chọn **"Open with"** → **"Excel"**

2. **Kiểm tra dữ liệu**
   - Xem columns có đúng không

3. **Save as Excel**
   - **Ctrl+S** hoặc **File** → **Save As**
   - Chọn format: **"Excel Workbook (.xlsx)"**
   - Tên file: `consultant-import-template.xlsx`
   - Click **Save**

✅ **XONG!**

---

### Cách 2: Dùng Google Sheets (1 phút)

1. Truy cập Google Drive
2. Upload file `consultant-import-template.csv`
3. Open with Google
Sheets
4. Download as Excel (.xlsx)

✅ **XONG!**

---

### Cách 3: Manual - Tạo file Excel từ đầu

1. **Mở Excel mới**
2. **Dòng 1**: Dán 16 header từ phần [📋 Dòng 1 - Header](#-dòng-1---header-tiếng-việt)
3. **Dòng 2+**: Nhập dữ liệu (hoặc copy từ phần [📝 Ví Dụ Dữ Liệu](#-ví-dụ-dữ-liệu---5-chuyên-gia))
4. **Save as**: `.xlsx` Excel format

---

### Cách 3: Copy-Paste CSV Data

File `consultant-import-template.csv` có sẵn dữ liệu:

1. **Mở Excel** → New Workbook
2. **Select all** trên CSV (copy nội dung dưới)
3. **Paste** vào Excel
4. Excel auto-detect columns bằng dấu phẩy
5. **Save as .xlsx**

**CSV Content** (copy-paste):
```
Mã Tư Vấn,Tên Tư Vấn,Email,Điện Thoại,Chuyên Môn,Danh Mục,Năm Kinh Nghiệm,Đánh Giá TB (0-5),Số Tư Vấn Hoàn Thành,Thời Gian Phản Hồi (Phút),Số Chat Tối Đa,Website,Địa Chỉ,Thành Phố,Mô Tả,Kích Hoạt
CONS-001,Nguyễn Văn Tư,nguyen@consultant.com,0123456789,nhập khẩu hóa chất; xuất khẩu,import; chemical; permit,15,4.8,150,2,5,https://example.com,123 Đường Lê Lợi; Quận 1,TP.HCM,15+ năm kinh nghiệm,YES
CONS-002,Trần Thị Tư Vấn,tran@consultant.com,0987654321,hóa chất; giấy phép,chemical; permit; environment,10,4.5,120,3,4,https://consulting.vn,456 Nguyễn Huệ; Quận 1,TP.HCM,10+ năm kinh nghiệm,YES
```


---

## ✅ Validation Checklist

Trước khi import, kiểm tra:

### Required Columns (PHẢI CÓ)
- ✅ **Cột A (Mã Tư Vấn)**: Không trống, không trùng lặp
- ✅ **Cột B (Tên Tư Vấn)**: Không trống
- ✅ **Cột C (Email)**: Format đúng (xxx@domain.com), không trùng lặp, không trống
- ✅ **Cột D (Điện Thoại)**: Không trống
- ✅ **Cột E (Chuyên Môn)**: Ít nhất 1 item, dùng dấu phẩy ngăn cách
- ✅ **Cột F (Danh Mục)**: Ít nhất 1 item, dùng dấu phẩy ngăn cách

### Optional Columns (CÓ THỂ TRỐNG)
- ❌ **Cột G (Năm KN)**: Để trống → mặc định = 0
- ❌ **Cột H (ĐG)**: Để trống → mặc định = 0.0; Nếu có → 0-5
- ❌ **Cột I (Số TƯ)**: Để trống → mặc định = 0
- ❌ **Cột J (Thời GiờPH)**: Để trống → mặc định = 5 phút
- ❌ **Cột K (Chat Max)**: Để trống → mặc định = 5
- ❌ **Cột L (Website)**: Để trống OK
- ❌ **Cột M (Địa Chỉ)**: Để trống OK
- ❌ **Cột N (TP)**: Để trống OK
- ❌ **Cột O (Mô Tả)**: Để trống OK
- ❌ **Cột P (Kích Hoạt)**: Để trống → mặc định = YES

---

## 🚀 Upload File

### Step 1: Test (Dry Run)
```bash
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
  -F "file=@consultant-import-template.xlsx"
```

**Phản hồi**:
- ✅ Nếu không lỗi → Toàn bộ thành công
- ❌ Nếu có lỗi → Fix và thử lại

### Step 2: Upload Thực Tế
```bash
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=false" \
  -F "file=@consultant-import-template.xlsx"
```

---

## 📌 Độ Rộng Cột Recommended (Excel)

| Cột | Tiêu Đề | Độ Rộng |
|-----|---------|---------|
| A | Mã Tư Vấn | 15 |
| B | Tên Tư Vấn | 18 |
| C | Email | 22 |
| D | Điện Thoại | 15 |
| E | Chuyên Môn | 30 |
| F | Danh Mục | 25 |
| G | Năm KN | 15 |
| H | Đánh Giá | 12 |
| I | Số TƯ | 18 |
| J | Thời Gian PH | 15 |
| K | Chat Max | 12 |
| L | Website | 20 |
| M | Địa Chỉ | 25 |
| N | TP | 12 |
| O | Mô Tả | 40 |
| P | Kích Hoạt | 12 |

---

## 🎯 Format Chi Tiết Từng Cột

### Cột A: Mã Tư Vấn
```
Ví dụ: CONS-001, CONS-ABC-2026, ABC-01
Không được: khoảng trắng thừa, ký tự lạ
```

### Cột B: Tên Tư Vấn
```
Ví dụ: Nguyễn Văn Tư, TS. Trần Thị B
Đặc biệt: Hỗ trợ Tiếng Việt đầy đủ
```

### Cột C: Email
```
Format: abc@domain.com hoặc abc@domain.co.vn
Ví dụ: nguyen@consultant.com, tran@company.vn
Yêu cầu: Hợp lệ, không trùng lặp (globally unique)
```

### Cột D: Điện Thoại
```
Ví dụ: 0123456789, +84123456789, (123) 456-7890
Yêu cầu: Chấp nhận format nhiều dạng
```

### Cột E & F: Chuyên Môn & Danh Mục
```
Format: Phẩy ngăn cách (comma-separated)
Ví dụ: "nhập khẩu hóa chất, xuất khẩu, môi trường"
       "import, chemical, permit"
Lưu ý: Không quá 10 items per cột
```

### Cột H: Đánh Giá
```
Format: Decimal từ 0 đến 5
Ví dụ: 4.8, 4.5, 3.0, 5
Xác thực: 0.0 <= rating <= 5.0
```

### Cột P: Kích Hoạt
```
Chấp nhận: YES, NO (case-insensitive)
Cũng chấp nhận: 1 (YES), 0 (NO)
Để trống: Mặc định = YES
```

---

## 🎁 Ready-to-Use Template CSV

**Tên file**: `consultant-template.csv`

```csv
Mã Tư Vấn,Tên Tư Vấn,Email,Điện Thoại,Chuyên Môn,Danh Mục,Năm Kinh Nghiệm,Đánh Giá TB (0-5),Số Tư Vấn Hoàn Thành,Thời Gian Phản Hồi (Phút),Số Chat Tối Đa,Website,Địa Chỉ,Thành Phố,Mô Tả,Kích Hoạt
CONS-001,Nguyễn Văn Tư,nguyen@consultant.com,0123456789,nhập khẩu hóa chất; xuất khẩu,import; chemical; permit,15,4.8,150,2,5,https://example.com,123 Lê Lợi; Q1,TP.HCM,15+ năm,YES
CONS-002,Trần Thị Tư Vấn,tran@consultant.com,0987654321,hóa chất; giấy phép,chemical; permit,10,4.5,120,3,4,,456 Nguyễn Huệ; Q1,TP.HCM,10+ năm,YES
CONS-003,Lê Văn Bình,le.van.binh@consultant.vn,0918765432,giấy phép; pháp luật,permit; legal,12,4.7,98,5,3,,789 Đường D; Q2,TP.HCM,Chuyên gia,YES
CONS-004,Phạm Thị Dung,pham.dung@consultant.com,0945123456,nhập khẩu; tuân thủ,import; compliance,8,4.3,67,4,3,https://phampham.com,101 Pasteur; Q3,TP.HCM,8 năm,YES
CONS-005,Đỗ Văn Minh,do.minh@expert.vn,0912345678,hóa chất; chất thải,chemical; waste,18,4.9,200,1,6,https://dominh.com,999 CMT8; Q3,TP.HCM,18 năm,YES
```

---

## ⚠️ Lỗi Phổ Biến & Cách Fix

| Lỗi | Nguyên Nhân | Fix |
|-----|-----------|-----|
| Email không hợp lệ | Format email sai | Dùng abc@domain.com |
| Email đã tồn tại | Trùng lặp email | Kiểm tra unique |
| Không có Chuyên Môn | Cột E trống | Nhập ít nhất 1 |
| Đánh giá > 5 | Rating > 5 | Phải <= 5 |
| Mã TƯ trùng | Cột A trùng lặp | Kiểm tra unique |
| Format sai | Sử dụng .xls thay .xlsx | Dùng .xlsx |

---

**Last Updated**: May 20, 2026  
**Version**: 1.0

