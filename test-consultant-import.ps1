#!/usr/bin/env pwsh
# Consultant Import System - Test & Demo Script
# Hướng dẫn test tính năng import consultant từ Excel

# ============================================
# BƯỚC 0: CONFIG
# ============================================

$API_URL = "http://localhost:8085/api"
$EXCEL_FILE = "consultants-import.xlsx"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🚀 Consultant Import System Test" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# ============================================
# BƯỚC 1: CHECK CONNECTION
# ============================================

Write-Host "📡 Checking API Connection..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "$API_URL/consultants/import/template" -ErrorAction SilentlyContinue
    Write-Host "✅ API is running" -ForegroundColor Green
} catch {
    Write-Host "❌ API is not running. Please start: java -jar target/RAG_API-1.0.0.jar" -ForegroundColor Red
    exit 1
}

# ============================================
# BƯỚC 2: CREATE TEST EXCEL FILE
# ============================================

Write-Host ""
Write-Host "📋 Creating test Excel file..." -ForegroundColor Yellow

# Chuẩn bị dữ liệu test
$consultantsData = @(
    @{
        "ConsultantCode" = "CONS-001"
        "ConsultantName" = "Nguyễn Văn Tư"
        "Email" = "nguyen@consultant.com"
        "Phone" = "0123456789"
        "Specializations" = "nhập khẩu hóa chất, xuất khẩu"
        "Categories" = "import, chemical"
        "ExperienceYears" = 15
        "AverageRating" = 4.8
        "CompletedConsultations" = 150
        "ResponseTimeMinutes" = 2
        "MaxConcurrentChats" = 5
        "Website" = "https://example.com"
        "Address" = "123 Đường A, Quận 1"
        "City" = "TP.HCM"
        "Description" = "15+ năm kinh nghiệm nhập khẩu hóa chất"
        "IsActive" = "YES"
    },
    @{
        "ConsultantCode" = "CONS-002"
        "ConsultantName" = "Trần Thị Tư Vấn"
        "Email" = "tran@consultant.com"
        "Phone" = "0987654321"
        "Specializations" = "hóa chất, giấy phép"
        "Categories" = "chemical, permit"
        "ExperienceYears" = 10
        "AverageRating" = 4.5
        "CompletedConsultations" = 120
        "ResponseTimeMinutes" = 3
        "MaxConcurrentChats" = 4
        "Website" = ""
        "Address" = "456 Đường B, Quận 5"
        "City" = "TP.HCM"
        "Description" = "10+ năm kinh nghiệm tư vấn hóa chất"
        "IsActive" = "YES"
    },
    @{
        "ConsultantCode" = "CONS-003"
        "ConsultantName" = "Lê Văn B"
        "Email" = "le@consultant.com"
        "Phone" = "0111111111"
        "Specializations" = "bảo vệ môi trường"
        "Categories" = "environmental"
        "ExperienceYears" = 8
        "AverageRating" = 4.2
        "CompletedConsultations" = 80
        "ResponseTimeMinutes" = 4
        "MaxConcurrentChats" = 3
        "Website" = "https://le-consultant.com"
        "Address" = ""
        "City" = "Hà Nội"
        "Description" = "8+ năm kinh nghiệm bảo vệ môi trường"
        "IsActive" = "YES"
    }
)

# Export to PSCustomObject for Excel
$consultantsData | Export-Csv -Path "consultants-test.csv" -NoTypeInformation -Encoding UTF8

Write-Host "✅ Test data created (consultants-test.csv)" -ForegroundColor Green
Write-Host "💡 Note: You need to convert CSV to XLSX using Excel or LibreOffice Calc" -ForegroundColor Cyan

# ============================================
# BƯỚC 3: TEST DRY RUN (Preview)
# ============================================

Write-Host ""
Write-Host "🧪 Testing DRY RUN Mode (Preview)..." -ForegroundColor Yellow
Write-Host "Command: " -ForegroundColor Cyan -NoNewline
Write-Host "POST /api/consultants/import?dryRun=true" -ForegroundColor White

if (Test-Path $EXCEL_FILE) {
    try {
        $response = Invoke-RestMethod -Uri "$API_URL/consultants/import?dryRun=true" `
            -Method Post `
            -ContentType "multipart/form-data" `
            -Form @{ file = Get-Item -Path $EXCEL_FILE }

        Write-Host ""
        Write-Host "Response:" -ForegroundColor Green
        Write-Host ($response | ConvertTo-Json -Depth 5) -ForegroundColor White

        if ($response.success) {
            Write-Host "✅ Dry run successful! No errors found." -ForegroundColor Green
        } else {
            Write-Host "⚠️  Dry run found errors:" -ForegroundColor Yellow
            $response.errorMessages | ForEach-Object { Write-Host "  - $_" }
        }
    } catch {
        Write-Host "❌ Error in dry run:" -ForegroundColor Red
        Write-Host $_.Exception.Message
    }
} else {
    Write-Host "⚠️  File not found: $EXCEL_FILE" -ForegroundColor Yellow
    Write-Host "Please create an Excel file with consultant data" -ForegroundColor Cyan
}

# ============================================
# BƯỚC 4: TEST REAL IMPORT
# ============================================

Write-Host ""
Write-Host "❓ Continue with real import? (Y/n)" -ForegroundColor Yellow
$continue = Read-Host

if ($continue -ne "n") {
    Write-Host ""
    Write-Host "📥 Testing REAL IMPORT (dryRun=false)..." -ForegroundColor Yellow
    Write-Host "Command: " -ForegroundColor Cyan -NoNewline
    Write-Host "POST /api/consultants/import?dryRun=false" -ForegroundColor White

    if (Test-Path $EXCEL_FILE) {
        try {
            $response = Invoke-RestMethod -Uri "$API_URL/consultants/import?dryRun=false" `
                -Method Post `
                -ContentType "multipart/form-data" `
                -Form @{ file = Get-Item -Path $EXCEL_FILE }

            Write-Host ""
            Write-Host "Response:" -ForegroundColor Green
            Write-Host ($response | ConvertTo-Json -Depth 5) -ForegroundColor White

            if ($response.success) {
                Write-Host ""
                Write-Host "✅ Import completed successfully!" -ForegroundColor Green
                Write-Host "Total: $($response.totalRows) rows, Success: $($response.successCount), Error: $($response.errorCount)" -ForegroundColor Green
            } else {
                Write-Host ""
                Write-Host "⚠️  Import had errors:" -ForegroundColor Yellow
                $response.errorMessages | ForEach-Object { Write-Host "  - $_" }
            }
        } catch {
            Write-Host "❌ Error in import:" -ForegroundColor Red
            Write-Host $_.Exception.Message
        }
    }
}

# ============================================
# BƯỚC 5: VERIFY DATA IN MONGODB
# ============================================

Write-Host ""
Write-Host "🔍 To verify imported data in MongoDB:" -ForegroundColor Cyan
Write-Host "  1. Open MongoDB Compass"
Write-Host "  2. Connect to: mongodb://localhost:27017/ragapi"
Write-Host "  3. Check collection: consultants"
Write-Host "  4. Look for newly imported consultants"

# ============================================
# CLEANUP
# ============================================

Write-Host ""
Write-Host ""
Write-Host "🧹 Cleanup:" -ForegroundColor Yellow
$cleanup = Read-Host "Delete test files? (y/n)"
if ($cleanup -eq "y") {
    Remove-Item -Path "consultants-test.csv" -ErrorAction SilentlyContinue
    Write-Host "✅ Test files deleted" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ Test Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan

