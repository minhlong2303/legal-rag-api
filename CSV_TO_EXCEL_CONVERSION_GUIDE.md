# 📊 CSV → Excel: Hướng Dẫn Chuyển Đổi (Vietnamese)

**Vấn đề**: File `consultant-import-template.csv` là **TEXT format**, không phải Excel binary (.xlsx)  
**Giải pháp**: Chuyển CSV sang Excel chỉ cần 3 click! 👇

---

## ✅ Cách 1: Dùng Microsoft Excel (30 giây)

### Step 1: Mở file CSV
1. Nhấp chuột phải vào `consultant-import-template.csv`
2. Chọn **"Open with"** → **"Excel"**
   ```
   consultant-import-template.csv 
   → Right-click 
   → "Open with" 
   → Choose "Excel"
   ```

### Step 2: Kiểm tra dữ liệu
- Excel tự động phát hiện columns bằng dấu phẩy `,`
- Xem dữ liệu có đúng không:
  ```
  Row 1: Headers (Tiếng Việt)
  Row 2: CONS-001 | Nguyễn Văn Tư | ...
  Row 3-6: Dữ liệu khác
  ```

### Step 3: Save as Excel
1. **Ctrl+S** hoặc **File** → **Save As**
2. Chọn định dạng **"Excel Workbook (.xlsx)"**
3. File name: `consultant-import-template.xlsx`
4. Click **Save**

✅ **XONG!** File Excel được tạo thành công!

---

## ✅ Cách 2: Dùng Google Sheets (1 phút)

Nếu không có Excel:

### Step 1: Upload CSV
1. Truy cập **Google Drive** (https://drive.google.com)
2. Click **"New"** → **"File upload"**
3. Chọn file `consultant-import-template.csv`

### Step 2: Open bằng Google Sheets
1. Right-click file
2. **"Open with"** → **"Google Sheets"**

### Step 3: Download as Excel
1. **File** → **Download** → **Microsoft Excel (.xlsx)**
2. File sẽ download là `consultant-import-template.xlsx`

✅ **XONG!** Có file Excel rồi!

---

## ✅ Cách 3: Dùng LibreOffice (Free)

Nếu không có Excel hay Google:

### Step 1: Tải LibreOffice
1. Truy cập: https://www.libreoffice.org
2. Tải **LibreOffice Calc** (free)
3. Cài đặt

### Step 2: Open CSV
1. Right-click `consultant-import-template.csv`
2. **"Open with"** → **"LibreOffice Calc"**
3. Click **OK** khi hỏi delimiter

### Step 3: Save as Excel
1. **File** → **Save As**
2. Format: **"Microsoft Excel 2007-365 (.xlsx)"**
3. Click **Save**

✅ **XONG!** Có file Excel rồi!

---

## 📝 Sau khi có file Excel (.xlsx)

### Upload vào API
```bash
# Test (Dry Run)
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=true" \
  -F "file=@consultant-import-template.xlsx"

# Actual Import
curl -X POST "http://localhost:8085/api/consultants/import?dryRun=false" \
  -F "file=@consultant-import-template.xlsx"
```

### Hoặc Upload qua UI
- Dùng form upload admin
- Chọn file `.xlsx`
- Click "Kiểm tra" (dryRun=true) trước
- Nếu OK, click "Import" (dryRun=false)

---

## ⚡ Quick Summary

| Cách | Công Cụ | Thời Gian | Dễ |
|------|---------|----------|-----|
| Cách 1 | Microsoft Excel | 30 giây | ⭐⭐⭐⭐⭐ |
| Cách 2 | Google Drive | 1 phút | ⭐⭐⭐⭐ |
| Cách 3 | LibreOffice | 2 phút | ⭐⭐⭐ |

**Recommend**: Cách 1 nếu có Excel, nếu không → Cách 2 (Google Sheets)

---

## 🎯 File Location

```
📍 D:\legal-rag-api-main\legal-rag-api-main\
   ├── consultant-import-template.csv  (TEXT - CSV format)
   └── consultant-import-template.xlsx  (BINARY - Excel format)
                                        ↑ Tạo sau khi convert
```

---

## ✨ Why CSV vs Excel?

**CSV** = Text format, nhỏ, dễ edit  
**Excel** = Binary format, có formatting, dùng để upload API  

**CSV** → Dữ liệu text  
**Excel** → Binary file + formatting (dùng cho upload)

---

**Last Updated**: May 20, 2026  
**Status**: Hướng dẫn chuyển đổi CSV → Excel (.xlsx)

