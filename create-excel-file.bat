@echo off
REM Create Excel file from CSV using PowerShell
REM This creates a real .xlsx binary file, not just text

cd /d "D:\legal-rag-api-main\legal-rag-api-main"

powershell -Command "
try {
    # Create Excel COM object
    \$excel = New-Object -ComObject Excel.Application
    \$excel.Visible = \$false
    \$workbook = \$excel.Workbooks.Add()
    \$ws = \$workbook.Sheets(1)
    \$ws.Name = 'Consultants'

    # Read CSV file
    \$csv = @'
Mã Tư Vấn,Tên Tư Vấn,Email,Điện Thoại,Chuyên Môn,Danh Mục,Năm Kinh Nghiệm,Đánh Giá TB (0-5),Số Tư Vấn Hoàn Thành,Thời Gian Phản Hồi (Phút),Số Chat Tối Đa,Website,Địa Chỉ,Thành Phố,Mô Tả,Kích Hoạt
CONS-001,Nguyễn Văn Tư,nguyen@consultant.com,0123456789,nhập khẩu hóa chất; xuất khẩu,import; chemical; permit,15,4.8,150,2,5,https://example.com,123 Đường Lê Lợi; Quận 1,TP.HCM,15+ năm kinh nghiệm trong nhập khẩu hóa chất,YES
CONS-002,Trần Thị Tư Vấn,tran@consultant.com,0987654321,hóa chất; giấy phép môi trường,chemical; permit; environment,10,4.5,120,3,4,https://tran-consulting.vn,456 Đường Nguyễn Huệ; Quận 1,TP.HCM,10+ năm tư vấn về quy định môi trường,YES
CONS-003,Lê Văn Bình,le.van.binh@consultant.vn,0918765432,giấy phép; pháp luật môi trường,permit; environment; legal,12,4.7,98,5,3,,789 Đường D; Quận 2,TP.HCM,Chuyên gia pháp luật môi trường hàng đầu,YES
CONS-004,Phạm Thị Dung,pham.dung@consultant.com,0945123456,nhập khẩu; tuân thủ quy định,import; compliance; chemical,8,4.3,67,4,3,https://phampham.com,101 Đường Pasteur; Quận 3,TP.HCM,8 năm kinh nghiệm tư vấn nhập khẩu,YES
CONS-005,Đỗ Văn Minh,do.minh@expert.vn,0912345678,hóa chất nguy hại; xử lý chất thải,chemical; waste; hazmat,18,4.9,200,1,6,https://dominh-experts.com,999 Đường Cách Mạng Tháng Tám; Quận 3,TP.HCM,18 năm chuyên gia hóa chất nguy hại,YES
'@

    # Parse CSV and add to Excel
    \$lines = \$csv -split \"`n\"
    \$row = 1

    foreach (\$line in \$lines) {
        if (\$line.Trim() -eq '') { continue }

        \$cells = \$line -split ','
        \$col = 1

        foreach (\$cell in \$cells) {
            \$cell = \$cell.Trim()
            \$ws.Cells(\$row, \$col).Value = \$cell
            \$col++
        }

        # Format header row
        if (\$row -eq 1) {
            \$headerRange = \$ws.Range('A1:P1')
            \$headerRange.Font.Bold = \$true
            \$headerRange.Font.Color = 16777215  # White
            \$headerRange.Interior.Color = 1976D2  # Blue
            \$headerRange.VerticalAlignment = -4108  # Center
            \$headerRange.HorizontalAlignment = -4108  # Center
            \$headerRange.WrapText = \$true
            \$ws.Rows(1).RowHeight = 30
        }

        \$row++
    }

    # Set column widths
    \$ws.Columns('A').ColumnWidth = 15
    \$ws.Columns('B').ColumnWidth = 18
    \$ws.Columns('C').ColumnWidth = 22
    \$ws.Columns('D').ColumnWidth = 15
    \$ws.Columns('E').ColumnWidth = 30
    \$ws.Columns('F').ColumnWidth = 25
    \$ws.Columns('G').ColumnWidth = 15
    \$ws.Columns('H').ColumnWidth = 12
    \$ws.Columns('I').ColumnWidth = 18
    \$ws.Columns('J').ColumnWidth = 15
    \$ws.Columns('K').ColumnWidth = 12
    \$ws.Columns('L').ColumnWidth = 20
    \$ws.Columns('M').ColumnWidth = 25
    \$ws.Columns('N').ColumnWidth = 12
    \$ws.Columns('O').ColumnWidth = 40
    \$ws.Columns('P').ColumnWidth = 12

    # Freeze header
    \$ws.Range('A2').Select()
    \$excel.ActiveWindow.FreezePanes = \$true

    # Save as Excel
    \$filepath = 'consultant-import-template.xlsx'
    \$workbook.SaveAs(\$filepath, 51)  # 51 = Excel 2007+ format
    \$workbook.Close()
    \$excel.Quit()

    # Get file info
    \$file = Get-Item \$filepath
    Write-Host \"✅ Excel file created successfully!\" -ForegroundColor Green
    Write-Host \"📊 File: \$(\$file.Name)\" -ForegroundColor Cyan
    Write-Host \"📦 Size: \$([Math]::Round(\$file.Length / 1KB, 2)) KB\" -ForegroundColor Cyan
    Write-Host \"📍 Location: \$(Get-Location)\\\$(\$file.Name)\" -ForegroundColor Cyan
    Write-Host \"\"
    Write-Host \"✨ You can now SELECT this file like a PDF!\" -ForegroundColor Yellow

} catch {
    Write-Host \"❌ Error: \$_\" -ForegroundColor Red
    Write-Host \"\"
    Write-Host \"Alternative: Open CSV with Excel and Save As .xlsx\" -ForegroundColor Yellow
}
"

pause

