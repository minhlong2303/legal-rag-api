#!/usr/bin/env powershell
# Test Consultant Import API
# Easy script to test the API locally

param(
    [ValidateSet("preview", "import", "template")]
    [string]$Mode = "preview",

    [string]$ExcelFile = "D:\legal-rag-api-main\legal-rag-api-main\consultant-import-template.xlsx",

    [string]$ApiUrl = "http://localhost:8085"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Consultant Import API Test Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if file exists
if ($Mode -ne "template" -and !(Test-Path $ExcelFile)) {
    Write-Host "ERROR: Excel file not found!" -ForegroundColor Red
    Write-Host "Expected: $ExcelFile" -ForegroundColor Yellow
    exit 1
}

# Test 1: Get Template
if ($Mode -eq "template") {
    Write-Host "TEST 1: Get Template Specifications" -ForegroundColor Green
    Write-Host "Endpoint: GET $ApiUrl/api/consultants/import/template" -ForegroundColor Cyan
    Write-Host ""

    try {
        $response = Invoke-RestMethod -Uri "$ApiUrl/api/consultants/import/template" -Method Get
        Write-Host "RESPONSE:" -ForegroundColor Green
        $response | ConvertTo-Json | Write-Host
        Write-Host ""
        Write-Host "Status: SUCCESS ✅" -ForegroundColor Green
    } catch {
        Write-Host "ERROR: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "Make sure server is running at $ApiUrl" -ForegroundColor Yellow
    }
    exit
}

# Test 2: Preview (dryRun=true)
if ($Mode -eq "preview") {
    Write-Host "TEST 2: Preview Import (dryRun=true)" -ForegroundColor Green
    Write-Host "Endpoint: POST $ApiUrl/api/consultants/import?dryRun=true" -ForegroundColor Cyan
    Write-Host "File: $ExcelFile" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Status: Sending request..." -ForegroundColor Yellow

    try {
        $form = @{
            file = Get-Item -Path $ExcelFile
        }

        $response = Invoke-RestMethod -Uri "$ApiUrl/api/consultants/import?dryRun=true" `
            -Method Post `
            -Form $form

        Write-Host ""
        Write-Host "RESPONSE:" -ForegroundColor Green
        $response | ConvertTo-Json | Write-Host
        Write-Host ""

        if ($response.success) {
            Write-Host "Status: PREVIEW SUCCESS ✅" -ForegroundColor Green
            Write-Host "Successful: $($response.successCount)" -ForegroundColor Green
            Write-Host "Failed: $($response.errorCount)" -ForegroundColor $(if($response.errorCount -eq 0) {"Green"} else {"Red"})
        } else {
            Write-Host "Status: PREVIEW FAILED ❌" -ForegroundColor Red
        }

    } catch {
        Write-Host "ERROR: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "Make sure server is running at $ApiUrl" -ForegroundColor Yellow
    }
    exit
}

# Test 3: Actual Import (dryRun=false)
if ($Mode -eq "import") {
    Write-Host "TEST 3: Actual Import (dryRun=false)" -ForegroundColor Green
    Write-Host "Endpoint: POST $ApiUrl/api/consultants/import?dryRun=false" -ForegroundColor Cyan
    Write-Host "File: $ExcelFile" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "WARNING: This will SAVE data to MongoDB!" -ForegroundColor Yellow
    Write-Host "Press [Y]es to continue or [N]o to cancel..." -ForegroundColor Yellow
    $confirm = Read-Host "[Y/N]"

    if ($confirm -ne "Y" -and $confirm -ne "y") {
        Write-Host "Import cancelled." -ForegroundColor Yellow
        exit
    }

    Write-Host "Status: Sending request..." -ForegroundColor Yellow

    try {
        $form = @{
            file = Get-Item -Path $ExcelFile
        }

        $response = Invoke-RestMethod -Uri "$ApiUrl/api/consultants/import?dryRun=false" `
            -Method Post `
            -Form $form

        Write-Host ""
        Write-Host "RESPONSE:" -ForegroundColor Green
        $response | ConvertTo-Json | Write-Host
        Write-Host ""

        if ($response.success) {
            Write-Host "Status: IMPORT SUCCESS ✅" -ForegroundColor Green
            Write-Host "Saved to MongoDB: $($response.successCount) consultants" -ForegroundColor Green
        } else {
            Write-Host "Status: IMPORT FAILED ❌" -ForegroundColor Red
        }

    } catch {
        Write-Host "ERROR: $($_.Exception.Message)" -ForegroundColor Red
    }
    exit
}

