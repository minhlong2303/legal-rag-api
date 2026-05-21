# Test API Forms - Command Line Cheatsheet
# Includes all field explanations + example values

# ==============================================
# TEST 1: Search Forms (Find matching forms)
# ==============================================

Write-Host "
╔════════════════════════════════════════════════════════╗
║ TEST 1: POST /api/forms/search                         ║
║ Purpose: Find matching forms by question               ║
╚════════════════════════════════════════════════════════╝

REQUEST:
  - question (REQUIRED) = User question to search
  - userId (OPTIONAL) = User ID (can be null)
  - purpose (OPTIONAL) = Hint (import/export/production/storage)

Only REQUIRED field is 'question'!
" -ForegroundColor Cyan

# Simplest version - only required field
$body1 = @{
    question = "Tôi muốn nhập khẩu hóa chất cấm"
} | ConvertTo-Json

Write-Host "Example 1 - SIMPLE (minimal):" -ForegroundColor Yellow
Write-Host $body1
Write-Host ""

Write-Host "Running test..." -ForegroundColor Green
$response1 = Invoke-RestMethod -Uri "http://localhost:8085/api/forms/search" `
    -Method Post `
    -Headers @{"Content-Type"="application/json"} `
    -Body $body1 `
    -ErrorAction SilentlyContinue

if ($response1) {
    Write-Host "✅ Response received!" -ForegroundColor Green
    Write-Host ($response1 | ConvertTo-Json -Depth 3)

    if ($response1.suggestions -and $response1.suggestions.Count -gt 0) {
        $selectedForm = $response1.suggestions[0]
        Write-Host "`n✅ IMPORTANT: Copy this formId for next test:" -ForegroundColor Green
        Write-Host "   formId = '$($selectedForm.formId)'" -ForegroundColor White
        $formId = $selectedForm.formId
        $formCode = $selectedForm.formCode
    }
} else {
    Write-Host "❌ No response - check if server is running" -ForegroundColor Red
}

# ==============================================
# TEST 2: Get Form Detail (Get form + auto-fill)
# ==============================================

Write-Host "`n
╔════════════════════════════════════════════════════════╗
║ TEST 2: POST /api/forms/detail                         ║
║ Purpose: Get form + auto-fill data                     ║
╚════════════════════════════════════════════════════════╝

FIELDS:
  - formId (REQUIRED) = From search results
  - userId (REQUIRED) = User to auto-fill from
  - autoFill (OPTIONAL) = Default true

Example:
" -ForegroundColor Cyan

$body2 = @{
    formId = "form-02a-import-banned-chemicals"
    userId = "user-test-001"
} | ConvertTo-Json

Write-Host $body2 -ForegroundColor Yellow
Write-Host ""

if ($formId) {
    Write-Host "Running test with actual formId from search..." -ForegroundColor Green
    $body2_real = @{
        formId = $formId
        userId = "user-test-001"
    } | ConvertTo-Json

    $response2 = Invoke-RestMethod -Uri "http://localhost:8085/api/forms/detail" `
        -Method Post `
        -Headers @{"Content-Type"="application/json"} `
        -Body $body2_real `
        -ErrorAction SilentlyContinue

    if ($response2) {
        Write-Host "✅ Response received!" -ForegroundColor Green
        Write-Host "Form name: $($response2.form.formName)" -ForegroundColor White
        Write-Host "Filled fields: $($response2.filledData.Count)" -ForegroundColor White
        Write-Host "Empty fields: $($response2.emptyFields.Count)" -ForegroundColor White
    }
} else {
    Write-Host "❌ Run TEST 1 first to get formId" -ForegroundColor Red
}

# ==============================================
# TEST 3: Update User Profile
# ==============================================

Write-Host "`n
╔════════════════════════════════════════════════════════╗
║ TEST 3: POST /api/forms/profile/update                 ║
║ Purpose: Update user info for auto-fill                ║
╚════════════════════════════════════════════════════════╝

FIELDS in URL:
  - userId = User to update

FIELDS in BODY:
  - organizationName
  - organizationPhone
  - organizationAddress
  - warehouseAddress
  - contactPersonName
  - contactPersonEmail
  - contactPersonPhone

" -ForegroundColor Cyan

$body3 = @{
    organizationName = "Công ty Hóa chất Việt Nam"
    organizationPhone = "0834567890"
    organizationAddress = "123 Nguyễn Trãi, Hà Nội"
    warehouseAddress = "Khu công nghiệp Thăng Long, Hà Nội"
    contactPersonName = "Nguyễn Văn An"
    contactPersonEmail = "an@company.vn"
    contactPersonPhone = "0912345678"
} | ConvertTo-Json

Write-Host $body3
Write-Host ""

Write-Host "Running test..." -ForegroundColor Green
$response3 = Invoke-RestMethod -Uri "http://localhost:8085/api/forms/profile/update?userId=user-test-001" `
    -Method Post `
    -Headers @{"Content-Type"="application/json"} `
    -Body $body3 `
    -ErrorAction SilentlyContinue

if ($response3) {
    Write-Host "✅ Response:" -ForegroundColor Green
    Write-Host ($response3 | ConvertTo-Json)
}

# ==============================================
# TEST 4: Submit Form
# ==============================================

Write-Host "`n
╔════════════════════════════════════════════════════════╗
║ TEST 4: POST /api/forms/submit                         ║
║ Purpose: Submit completed form                         ║
╚════════════════════════════════════════════════════════╝

FIELDS:
  - formId (REQUIRED)
  - userId (REQUIRED)
  - formData (REQUIRED) = ALL fields (filled + user input)

" -ForegroundColor Cyan

$body4 = @{
    formId = "form-02a-import-banned-chemicals"
    userId = "user-test-001"
    formData = @{
        organizationName = "Công ty Hóa chất Việt Nam"
        organizationPhone = "0834567890"
        organizationAddress = "123 Nguyễn Trãi, Hà Nội"
        warehouseAddress = "Khu công nghiệp Thăng Long"
        contactPersonName = "Nguyễn Văn An"
        chemicalName = "Chlorine Gas"
        casNumber = "7782-50-5"
        quantity = "1000"
        quantityUnit = "kg"
        importPurpose = "Sử dụng trong sản xuất"
        permissionNumber = "QD-2024-001"
        issueDate = "2024-01-15"
        expiryDate = "2025-01-15"
    }
} | ConvertTo-Json

Write-Host $body4
Write-Host ""

Write-Host "Running test..." -ForegroundColor Green
if ($formId) {
    $body4_real = @{
        formId = $formId
        userId = "user-test-001"
        formData = @{
            organizationName = "Công ty Hóa chất Việt Nam"
            organizationPhone = "0834567890"
            organizationAddress = "123 Nguyễn Trãi, Hà Nội"
            warehouseAddress = "Khu công nghiệp Thăng Long"
            contactPersonName = "Nguyễn Văn An"
            chemicalName = "Chlorine Gas"
            casNumber = "7782-50-5"
            quantity = 1000
            quantityUnit = "kg"
            importPurpose = "Sử dụng trong sản xuất"
            permissionNumber = "QD-2024-001"
            issueDate = "2024-01-15"
            expiryDate = "2025-01-15"
        }
    } | ConvertTo-Json

    $response4 = Invoke-RestMethod -Uri "http://localhost:8085/api/forms/submit" `
        -Method Post `
        -Headers @{"Content-Type"="application/json"} `
        -Body $body4_real `
        -ErrorAction SilentlyContinue

    if ($response4) {
        Write-Host "✅ Response:" -ForegroundColor Green
        Write-Host ($response4 | ConvertTo-Json)
    }
} else {
    Write-Host "❌ Run TEST 1 first to get formId" -ForegroundColor Red
}

# ==============================================
# TEST 5: Download Form (PDF)
# ==============================================

Write-Host "`n
╔════════════════════════════════════════════════════════╗
║ TEST 5: POST /api/forms/download                       ║
║ Purpose: Download form as PDF or Excel                 ║
╚════════════════════════════════════════════════════════╝

FIELDS:
  - formId (REQUIRED)
  - userId (REQUIRED)
  - format (REQUIRED) = 'PDF' or 'EXCEL'
  - formData (REQUIRED) = Form data to export

Format values:
  'PDF'   → .pdf file
  'EXCEL' → .csv file

" -ForegroundColor Cyan

$body5 = @{
    formId = "form-02a-import-banned-chemicals"
    userId = "user-test-001"
    format = "PDF"
    formData = @{
        organizationName = "Công ty Hóa chất Việt Nam"
        chemicalName = "Chlorine Gas"
        casNumber = "7782-50-5"
        quantity = 1000
    }
} | ConvertTo-Json

Write-Host $body5
Write-Host ""

Write-Host "Running test..." -ForegroundColor Green
if ($formId) {
    $body5_real = @{
        formId = $formId
        userId = "user-test-001"
        format = "PDF"
        formData = @{
            organizationName = "Công ty Hóa chất Việt Nam"
            chemicalName = "Chlorine Gas"
            casNumber = "7782-50-5"
            quantity = 1000
        }
    } | ConvertTo-Json

    try {
        $response5 = Invoke-RestMethod -Uri "http://localhost:8085/api/forms/download" `
            -Method Post `
            -Headers @{"Content-Type"="application/json"} `
            -Body $body5_real

        Write-Host "✅ PDF file generated! Size: $($response5.Length) bytes" -ForegroundColor Green

        # Save file
        $fileName = "form_$($formCode)_$(Get-Date -Format 'yyyyMMdd_HHmmss').pdf"
        [System.IO.File]::WriteAllBytes($fileName, $response5)
        Write-Host "✅ Saved to: $fileName" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error: $_" -ForegroundColor Red
    }
} else {
    Write-Host "❌ Run TEST 1 first to get formId" -ForegroundColor Red
}

# ==============================================
# SUMMARY
# ==============================================

Write-Host "`n
╔════════════════════════════════════════════════════════╗
║ TESTS COMPLETED                                        ║
╚════════════════════════════════════════════════════════╝

For more details, read:
  → API_REQUEST_RESPONSE_DETAILED.md

Key points:
  ✓ Only 'question' is required for search
  ✓ formId is from search response
  ✓ userId can be any test value
  ✓ formData must have ALL fields
  ✓ format is case-sensitive (PDF, EXCEL)

" -ForegroundColor Cyan

