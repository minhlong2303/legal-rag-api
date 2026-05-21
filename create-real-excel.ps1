# Create real Excel file from CSV

$csvFile = "D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.csv"
$xlsxFile = "D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx"

Write-Host "Creating Excel file..."  -ForegroundColor Cyan

$data = Import-Csv -Path $csvFile -Encoding UTF8

$excel = New-Object -ComObject Excel.Application
$excel.Visible = $false
$excel.DisplayAlerts = $false

$wb = $excel.Workbooks.Add()
$ws = $wb.Sheets(1)
$ws.Name = "Consultants"

$headers = ($data | Select-Object -First 1).PSObject.Properties.Name
for ($col = 0; $col -lt $headers.Count; $col++) {
    $ws.Cells(1, $col + 1).Value = $headers[$col]
}

$headerRange = $ws.Range("A1:P1")
$headerRange.Font.Bold = $true
$headerRange.Font.Color = 16777215
$headerRange.Interior.Color = 1976727
$headerRange.HorizontalAlignment = -4108
$headerRange.VerticalAlignment = -4108
$headerRange.WrapText = $true
$ws.Rows(1).RowHeight = 25

$rowNum = 2
foreach ($row in $data) {
    $colNum = 1
    foreach ($header in $headers) {
        $ws.Cells($rowNum, $colNum).Value = $row.$header
        $colNum++
    }
    $rowNum++
}

$widths = @(15, 18, 22, 15, 30, 25, 15, 12, 18, 15, 12, 20, 25, 12, 40, 12)
for ($i = 0; $i -lt $widths.Count; $i++) {
    $ws.Columns($i + 1).ColumnWidth = $widths[$i]
}

$ws.Range("A2").Select() | Out-Null
$excel.ActiveWindow.FreezePanes = $true

$wb.SaveAs($xlsxFile, 51)
$wb.Close($false)
$excel.Quit()

$file = Get-Item $xlsxFile
Write-Host "SUCCESS! Excel file created!" -ForegroundColor Green
Write-Host "File: $($file.Name)" -ForegroundColor Cyan
Write-Host "Size: $([Math]::Round($file.Length / 1KB, 2)) KB" -ForegroundColor Cyan
Write-Host "Path: $xlsxFile" -ForegroundColor Cyan
Write-Host "REAL Excel file - can browse and select now!" -ForegroundColor Yellow




