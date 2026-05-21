# API Test Examples for RAG Legal Service with Markdown Format (PowerShell)

$BaseUrl = "http://localhost:8085"

Write-Host "================================" -ForegroundColor Cyan
Write-Host "🎯 RAG API - Format Response Test" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: BRIEF (Tóm tắt)
Write-Host "1️⃣ TEST BRIEF (Tóm tắt)" -ForegroundColor Green
Write-Host "Request:"
Write-Host "  POST /api/ai/query"
Write-Host '  {"question": "Nhà máy xử lý chất thải cần giấy phép gì?", "detailLevel": "brief"}'
Write-Host ""
Write-Host "Sending request..." -ForegroundColor Yellow

$body1 = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "brief"
} | ConvertTo-Json

$response1 = Invoke-RestMethod -Uri "$BaseUrl/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body1

Write-Host "Response:" -ForegroundColor Green
$response1 | ConvertTo-Json | Write-Host

Write-Host ""
Write-Host "---" -ForegroundColor Gray
Write-Host ""

# Test 2: NORMAL (Vừa phải - mặc định)
Write-Host "2️⃣ TEST NORMAL (Vừa phải)" -ForegroundColor Green
Write-Host "Request:"
Write-Host "  POST /api/ai/query"
Write-Host '  {"question": "Nhà máy xử lý chất thải cần giấy phép gì?"}'
Write-Host ""
Write-Host "Sending request..." -ForegroundColor Yellow

$body2 = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
} | ConvertTo-Json

$response2 = Invoke-RestMethod -Uri "$BaseUrl/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body2

Write-Host "Response:" -ForegroundColor Green
$response2 | ConvertTo-Json | Write-Host

Write-Host ""
Write-Host "---" -ForegroundColor Gray
Write-Host ""

# Test 3: DETAILED (Chi tiết)
Write-Host "3️⃣ TEST DETAILED (Chi tiết)" -ForegroundColor Green
Write-Host "Request:"
Write-Host "  POST /api/ai/query"
Write-Host '  {"question": "Nhà máy xử lý chất thải cần giấy phép gì?", "detailLevel": "detailed"}'
Write-Host ""
Write-Host "Sending request..." -ForegroundColor Yellow

$body3 = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "detailed"
} | ConvertTo-Json

$response3 = Invoke-RestMethod -Uri "$BaseUrl/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body3

Write-Host "Response:" -ForegroundColor Green
$response3 | ConvertTo-Json | Write-Host

Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
Write-Host "✅ Test Complete!" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Notes:" -ForegroundColor Yellow
Write-Host "  - Hãy chắc chắn server đang chạy trên port 8085"
Write-Host "  - Response sẽ là JSON với field 'answer' chứa Markdown format"
Write-Host "  - Bạn có thể renders Markdown trong UI bằng markdown libraries (React Markdown, Marked.js, etc.)"
Write-Host ""

