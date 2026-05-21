# 🔧 Troubleshooting Guide - LLM Service Error

## ❌ Lỗi: "Lỗi máy chủ: Không thể gọi tới dịch vụ LLM"

Lỗi này xuất hiện khi không thể kết nối tới **OpenRouter API** hoặc các service khác.

---

## 🔍 Step 1: Chạy Diagnostics

### Via API (Recommended)
```powershell
# Test API endpoint
$response = Invoke-RestMethod -Uri "http://localhost:8085/api/health/llm-diagnostics" `
    -Method Get

$response | ConvertTo-Json | Write-Host
```

**Expected Response:**
```json
{
  "diagnostics": {
    "apiKeyValid": true,
    "openRouterConnectivity": true,
    "openRouterApiTest": "KEY_FORMAT_OK",
    "ollamaConnectivity": true,
    "configDetails": {
      "OpenRouter Base URL": "https://openrouter.ai/api/v1",
      "OpenRouter Model": "deepseek/deepseek-v4-flash:free",
      "Ollama Base URL": "http://localhost:11434",
      "Embedding Model": "embeddinggemma",
      "API Key (masked)": "sk-or-v1-xxx...xxx"
    },
    "overallStatus": "✅ OK: All services appear to be configured correctly"
  }
}
```

### Via Server Logs
Chạy server và xem logs khi gửi request:
```powershell
java -jar target/RAG_API-1.0.0.jar 2>&1 | Tee-Object logs.txt
```

Tìm section "========== LLM API ERROR ==========" trong logs để xem chi tiết.

---

## 🚨 Diagnostic Results & Solutions

### ❌ **apiKeyValid: FALSE**

**Problem:** OpenRouter API Key không được cấu hình hoặc không hợp lệ

**Solutions:**

1. **Kiểm tra API Key format:**
   ```powershell
   $key = "sk-or-v1-your-actual-key-here"
   Write-Host "Key starts with: $($key.Substring(0, 10))"
   Write-Host "Key length: $($key.Length)"
   # Expected: starts with "sk-or-v1-", length > 50
   ```

2. **Check configuration file:**
   - File: `src/main/resources/application.yml`
   - Section: `openrouter:`
   - Verify: `api-key` field is not empty or placeholder

   ```yaml
   openrouter:
     api-key: sk-or-v1-YOUR_ACTUAL_KEY  # ✓ Should not contain "YOUR_KEY"
   ```

3. **Using Environment Variables (Recommended):**
   ```powershell
   $env:OPENROUTER_API_KEY = "sk-or-v1-your-actual-key-here"
   ```

   Then in `application.yml`:
   ```yaml
   openrouter:
     api-key: ${OPENROUTER_API_KEY}
   ```

4. **Get new API Key:**
   - Go to: https://openrouter.ai/
   - Sign up or login
   - Navigate to API Keys section
   - Create new API key
   - Copy and paste in configuration

---

### ❌ **openRouterConnectivity: FALSE**

**Problem:** Cannot connect to OpenRouter API server

**Root Causes:**
1. Network/Firewall blocking
2. OpenRouter server is down
3. Wrong base URL

**Solutions:**

1. **Test connectivity manually:**
   ```powershell
   # Test if OpenRouter is reachable
   $response = Invoke-WebRequest -Uri "https://openrouter.ai/api/v1" -Method Head
   Write-Host "Status Code: $($response.StatusCode)"
   ```

2. **Check firewall:**
   ```powershell
   # Windows Firewall check
   netsh advfirewall show domainprofile | findstr State
   netsh advfirewall show publicprofile | findstr State
   ```

   Nếu firewall enabled, ensure https (port 443) is allowed.

3. **Verify base URL in config:**
   ```yaml
   openrouter:
     base-url: https://openrouter.ai/api/v1  # ✓ Correct
   ```

4. **Check network:**
   ```powershell
   # Ping OpenRouter DNS
   Test-NetConnection -ComputerName openrouter.ai -Port 443
   
   # Expected: TcpTestSucceeded: True
   ```

5. **Proxy/VPN issues:**
   - If using corporate proxy/VPN, ensure it allows https://openrouter.ai
   - Check proxy settings in Java if applicable

---

### ❌ **openRouterApiTest: INVALID_KEY_FORMAT**

**Problem:** API Key format is invalid

**Solutions:**

1. OpenRouter keys should start with `sk-or-v1-`
   ```powershell
   $key = "YOUR_KEY_HERE"
   if ($key.StartsWith("sk-or")) {
       Write-Host "✓ Key format OK"
   } else {
       Write-Host "✗ Key format WRONG - should start with sk-or"
   }
   ```

2. Minimum length should be 50 characters
   ```powershell
   if ($key.Length -gt 50) {
       Write-Host "✓ Key length OK"
   } else {
       Write-Host "✗ Key too short"
   }
   ```

3. Get correct key from https://openrouter.ai/keys

---

### ❌ **ollamaConnectivity: FALSE**

**Problem:** Cannot connect to Ollama embedding service

**Root Causes:**
1. Ollama not running
2. Ollama running on different port/host
3. Network issue

**Solutions:**

1. **Start Ollama:**
   ```powershell
   # Download from: https://ollama.ai
   # Install and run:
   ollama serve
   
   # Default: http://localhost:11434
   ```

2. **Check Ollama is running:**
   ```powershell
   Test-NetConnection -ComputerName localhost -Port 11434
   
   # Or with curl
   curl http://localhost:11434/api/tags
   ```

3. **Verify embedding model exists:**
   ```powershell
   # Check available models
   Invoke-WebRequest -Uri "http://localhost:11434/api/tags" | ConvertFrom-Json | Write-Host
   
   # Pull embedding model if missing
   ollama pull embeddinggemma
   ```

4. **Check configuration:**
   ```yaml
   ollama:
     base-url: http://localhost:11434
     embedding-model: embeddinggemma
   ```

   If Ollama on different machine:
   ```yaml
   ollama:
     base-url: http://192.168.x.x:11434
   ```

---

### ✅ **overallStatus: All OK**

**If all diagnostics pass but query still fails:**

#### Option 1: Check Server Logs
```powershell
# Run server with full logging
java -jar target/RAG_API-1.0.0.jar > logs.txt 2>&1

# Wait for error, then check logs
Get-Content logs.txt | Select-String -Pattern "LLM API ERROR" -Context 20
```

#### Option 2: Test Query Directly
```powershell
$body = @{
    question = "Test question"
    detailLevel = "brief"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

Write-Host "Response: $($response.answer)"
```

#### Option 3: Check Rate Limits

OpenRouter free tier has rate limits:
```powershell
# Check if you've exceeded quota
# Visit https://openrouter.ai/activity

# Solution: Wait or upgrade to paid plan
```

#### Option 4: Check Model Availability

DeepSeek model on OpenRouter might be temporarily unavailable:
```yaml
# Try alternative model in application.yml
openrouter:
  model: openrouter/auto  # Auto-select available model
  # Or specific: mistralai/mistral-7b
```

---

## 📝 Common Error Messages in Logs

### Error 1: Connection Refused
```
ERROR: Connection refused to openrouter.ai:443
Solution: Check firewall, network connectivity, or OpenRouter is down
```

### Error 2: 401 Unauthorized
```
ERROR: 401 Unauthorized from OpenRouter API
Solution: Check API key is valid, not expired
```

### Error 3: 429 Too Many Requests
```
ERROR: 429 Too Many Requests (Rate limit exceeded)
Solution: Wait before retrying, or upgrade OpenRouter plan
```

### Error 4: 500 Internal Server Error (OpenRouter)
```
ERROR: 500 Internal Server Error from OpenRouter
Solution: OpenRouter server issue, wait and retry
```

### Error 5: Socket Timeout
```
ERROR: Socket timeout connecting to OpenRouter
Solution: Network slow, increase timeout in config:
openrouter:
  timeout-seconds: 600  # Increase from default 300
```

### Error 6: Ollama Connection Refused
```
ERROR: Connection refused to Ollama on http://localhost:11434
Solution: Start Ollama, or change base-url if running elsewhere
```

---

## 🧪 Complete Debug Workflow

### Step 1: Run Diagnostics
```powershell
$diag = Invoke-RestMethod -Uri "http://localhost:8085/api/health/llm-diagnostics" -Method Get
$diag.diagnostics.overallStatus | Write-Host
```

### Step 2: Check Logs
```powershell
# Run server and capture logs
cd "D:\legal-rag-api-main\legal-rag-api-main"
java -jar target/RAG_API-1.0.0.jar | Tee-Object server.log &

# Wait 5 seconds
Start-Sleep -Seconds 5

# Query to trigger error
$body = @{question = "test"} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" -Method Post -ContentType "application/json" -Body $body

# Check logs for error details
Get-Content server.log | Select-String -Pattern "LLM API ERROR", "Error Type", "Root Cause" | Select-Object -Last 10
```

### Step 3: Isolate Problem
Based on logs:
- **If API key error** → Re-check key in config
- **If connection error** → Check network/firewall
- **If timeout error** → Increase timeout in config
- **If 429/rate limit** → Wait or upgrade plan
- **If Ollama error** → Start Ollama or check config

### Step 4: Retry Query
```powershell
$body = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "normal"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

$response.answer | Write-Host
```

---

## ⚡ Quick Fixes

### Fix 1: Restart Services
```powershell
# Kill existing server
Get-Process | Where-Object {$_.ProcessName -eq "java"} | Stop-Process -Force

# Restart
cd "D:\legal-rag-api-main\legal-rag-api-main"
java -jar target/RAG_API-1.0.0.jar
```

### Fix 2: Update Configuration
```yaml
openrouter:
  api-key: YOUR_NEW_KEY_HERE
  timeout-seconds: 600      # Increase timeout
  
ollama:
  base-url: http://localhost:11434
  timeout-seconds: 300
```

Rebuild:
```powershell
mvn clean package -DskipTests
java -jar target/RAG_API-1.0.0.jar
```

### Fix 3: Check Dependencies
```powershell
# Might be missing langchain4j libraries
mvn dependency:tree | grep langchain4j

# Rebuild with dependencies
mvn clean install
mvn package -DskipTests
```

---

## 📋 Checklist Before Querying

- [ ] OpenRouter account created and API key obtained
- [ ] API key pasted in `application.yml` under `openrouter.api-key`
- [ ] `openrouter.base-url` is `https://openrouter.ai/api/v1`
- [ ] `openrouter.model` is set to `deepseek/deepseek-v4-flash:free`
- [ ] Ollama is running: `ollama serve` (if using Ollama embeddings)
- [ ] Ollama model exists: `ollama pull embeddinggemma`
- [ ] MongoDB is running: `docker ps | grep mongodb`
- [ ] Elasticsearch is running: `docker ps | grep elasticsearch`
- [ ] Server started without errors: `mvn spring-boot:run`
- [ ] Diagnostics endpoint returns "✅ OK": `/api/health/llm-diagnostics`

---

## 🚀 Testing After Fix

Once diagnostics pass, test full workflow:

```powershell
# 1. Upload document
$uploadBody = @{
    title = "Test Regulation"
    category = "environment"
    content = "Nhà máy xử lý chất thải tại TP.HCM phải có giấy phép môi trường..."
} | ConvertTo-Json

$uploadResp = Invoke-RestMethod -Uri "http://localhost:8085/api/documents" `
    -Method Post `
    -ContentType "application/json" `
    -Body $uploadBody

Write-Host "Upload: $($uploadResp.message)"

# 2. Query AI
$queryBody = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "detailed"
} | ConvertTo-Json

$queryResp = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $queryBody

Write-Host "Answer:`n$($queryResp.answer)"
```

Expected: AI returns formatted answer with Markdown structure.

---

## 📞 Need More Help?

1. **Check logs:** Server logs contain detailed error messages
2. **Run diagnostics:** `/api/health/llm-diagnostics` shows service status
3. **Test connectivity:** Use PowerShell commands above to test each service
4. **Check documentation:** See README.md, DEPLOYMENT_GUIDE.md, FORMAT_RESPONSE_GUIDE.md

---

**Last Updated:** May 16, 2026  
**Status:** Complete

