# 🚀 Deployment & Usage Guide - RAG API with Markdown Format

## 📦 Cài Đặt & Deployment

### Step 1: Chuẩn Bị Môi Trường

**Requirements:**
- Java 17+ (verify: `java -version`)
- Docker (for MongoDB & Elasticsearch)
- Maven 3.8+ (or use IDE build)

### Step 2: Khởi Động Services

#### 2.1 MongoDB
```powershell
# Windows PowerShell
docker run -d --name mongodb -p 27017:27017 mongo:latest

# Verify
docker ps | Select-Object NAMES, PORTS
```

#### 2.2 Elasticsearch
```powershell
# Windows PowerShell
docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:8.13.0

# Verify
Invoke-WebRequest http://localhost:9200 | Select-Object StatusCode
```

#### 2.3 Create Elasticsearch Index
```powershell
$body = @{
    mappings = @{
        properties = @{
            documentId = @{type = "keyword"}
            content = @{type = "text"}
            vector = @{
                type = "dense_vector"
                dims = 1536
                index = $true
                similarity = "cosine"
            }
        }
    }
} | ConvertTo-Json -Depth 10

Invoke-WebRequest -Uri "http://localhost:9200/legal_vectors" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $body

# Response should be: {"acknowledged":true,"shards_acknowledged":true,"index":"legal_vectors"}
```

### Step 3: Configure OpenAI API Key

Edit: `src/main/resources/application.yml`

```yaml
openai:
  api-key: sk-proj-YOUR_ACTUAL_API_KEY  # Thay bằng key thực của bạn
  model: gpt-4-1-mini-2024-07-18        # Hoặc model khác
```

> **⚠️ Lưu ý**: Bảo vệ API key, không commit lên GitHub. Dùng environment variables nếu cần:
```yaml
openai:
  api-key: ${OPENAI_API_KEY}
```

Rồi set environment variable:
```powershell
$env:OPENAI_API_KEY = "sk-proj-YOUR_KEY"
```

### Step 4: Build Project

```powershell
cd "D:\legal-rag-api-main\legal-rag-api-main"

# Build
mvn clean package -DskipTests

# Hoặc nếu chưa cài Maven, từ IDE (IntelliJ):
# Menu: Build → Build Project (Ctrl+F9)
```

### Step 5: Run Server

```powershell
cd "D:\legal-rag-api-main\legal-rag-api-main"
java -jar target/RAG_API-1.0.0.jar
```

**Expected Output:**
```
...
Started RagApiApplication in X.XXX seconds
```

Server sẽ lắng nghe trên `http://localhost:8085`

---

## 🔍 Testing & Usage

### Method 1: Swagger UI (Browser)
```
http://localhost:8085/swagger-ui.html
```
UI interactive để test tất cả endpoints.

### Method 2: PowerShell (Windows)

**Run test script:**
```powershell
cd "D:\legal-rag-api-main\legal-rag-api-main"
.\test-api.ps1
```

Hoặc test từng endpoint riêng:

#### 2.1 Upload Document
```powershell
$body = @{
    title = "Luật Bảo vệ Môi trường"
    category = "environment"
    content = "Nhà máy xử lý chất thải tại TP.HCM phải có giấy phép môi trường..."
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8085/api/documents" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

#### 2.2 Query AI (Default/Normal)
```powershell
$body = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

# Print answer (with Markdown format)
Write-Host $response.answer
```

#### 2.3 Query AI (Brief)
```powershell
$body = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "brief"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

Write-Host $response.answer
```

#### 2.4 Query AI (Detailed)
```powershell
$body = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "detailed"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

Write-Host $response.answer
```

### Method 3: cURL (Bash/Git Bash)

```bash
# BRIEF
curl -X POST http://localhost:8085/api/ai/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Nhà máy xử lý chất thải cần giấy phép gì?",
    "detailLevel": "brief"
  }' | jq .answer -r

# NORMAL (default)
curl -X POST http://localhost:8085/api/ai/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Nhà máy xử lý chất thải cần giấy phép gì?"
  }' | jq .answer -r

# DETAILED
curl -X POST http://localhost:8085/api/ai/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Nhà máy xử lý chất thải cần giấy phép gì?",
    "detailLevel": "detailed"
  }' | jq .answer -r
```

---

## 🎨 Rendering Markdown Response

Response từ API chứa **Markdown format**. Để render đẹp, dùng:

### Frontend (JavaScript/React)

#### Using `react-markdown`:
```javascript
import ReactMarkdown from 'react-markdown';

function QueryResponse({ answer }) {
  return <ReactMarkdown>{answer}</ReactMarkdown>;
}
```

#### Using `marked.js`:
```javascript
import { marked } from 'marked';

function QueryResponse({ answer }) {
  return (
    <div dangerouslySetInnerHTML={{ __html: marked(answer) }} />
  );
}
```

#### Using HTML `<pre>` (basic):
```html
<pre style="white-space: pre-wrap; word-wrap: break-word;">{{ answer }}</pre>
```

### Backend (Store in DB)
```java
// Save Markdown directly in database
String markdownAnswer = ragService.ask(question, "detailed");
Document doc = new Document("question_id", question, markdownAnswer, LocalDateTime.now());
repository.save(doc);
```

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| `Connection refused :27017` | Bắt đầu MongoDB: `docker run -d -p 27017:27017 mongo:latest` |
| `Connection refused :9200` | Bắt đầu Elasticsearch: `docker run -d -p 9200:9200 -e "discovery.type=single-node" elasticsearch:8.13.0` |
| `{"error":"no such index"}` | Tạo index: xem bước 2.3 ở trên |
| `401 Unauthorized` | Kiểm tra OpenAI API key trong `application.yml` |
| `LLM returned empty response` | Kiểm tra logs, có thể API quota hết hoặc model không sử dụng được |
| `Cannot invoke….choices()` | Bộ lọc exception đã được thêm, check logs xem error gốc |

**Check Logs:**
```powershell
# Redirect logs to file
java -jar target/RAG_API-1.0.0.jar > logs.txt 2>&1
```

---

## 📊 Architecture & Flow

### Ingestion
```
User Upload PDF
    ↓
PdfExtractionService (extract text)
    ↓
VietnameseLegalChunkingService (chunk 500-char chunks)
    ↓
EmbeddingService (call OpenAI text-embedding-3-small)
    ↓
ElasticVectorService (index vectors to Elasticsearch)
    ↓
MongoDB (save document metadata)
```

### Query (RAG)
```
User Question
    ↓
EmbeddingService (embed question)
    ↓
ElasticVectorService.search() (KNN search k=5 chunks)
    ↓
RagService.ask() 
    - Build Markdown prompt based on detailLevel
    - Call OpenAI GPT-4.1-mini
    ↓
AI-generated Answer (Markdown format)
```

### Detail Levels
```
brief
  └─ ## Heading + ** bold ** + citation (3-4 sections)

normal (default)
  └─ ## Heading + ### subheadings + bullets + numbered lists

detailed
  └─ All of above + examples + exceptions + deep explanations
```

---

## 🧪 Performance Tips

1. **Cache embeddings**: Nếu cùng question hỏi nhiều lần, cache embedding result
2. **Batch indexing**: Upload nhiều documents cùng lúc (nếu possible)
3. **Monitor Elasticsearch**: Check Elasticsearch cluster health:
   ```powershell
   Invoke-WebRequest http://localhost:9200/_cluster/health | ConvertFrom-Json
   ```
4. **Detail level trade-off**: 
   - `brief` → nhanh hơn (prompt nhỏ)
   - `detailed` → đầy đủ nhưng chậm hơn (prompt lớn)

---

## 📝 Example Full Workflow

### 1. Upload Legal Document:
```powershell
$uploadBody = @{
    title = "Quy định xử lý chất thải"
    category = "environment"
    content = "Nhà máy xử lý chất thải tại TP.HCM phải có giấy phép môi trường (tích hợp các loại giấy phép thành phần). Nếu xử lý rác thải nguy hại, cần bổ sung thêm chức năng/giấy phép hành nghề quản lý chất thải nguy hại do Bộ Tài nguyên và Môi trường cấp. Thời hạn cấp giấy phép môi trường là 5 năm, có thể gia hạn. Cơ sở phải tuân thủ các quy chuẩn kỹ thuật quốc gia về xử lý chất thải..."
} | ConvertTo-Json

$uploadResp = Invoke-RestMethod -Uri "http://localhost:8085/api/documents" `
    -Method Post `
    -ContentType "application/json" `
    -Body $uploadBody

Write-Host "✅ Document uploaded: $($uploadResp.message)"
```

### 2. Query with Different Detail Levels:

```powershell
# BRIEF (nhanh, ngắn)
$query1 = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "brief"
} | ConvertTo-Json

$resp1 = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $query1

Write-Host "BRIEF ANSWER:`n$($resp1.answer)`n"

# NORMAL (cân bằng)
$query2 = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
} | ConvertTo-Json

$resp2 = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $query2

Write-Host "NORMAL ANSWER:`n$($resp2.answer)`n"

# DETAILED (đầy đủ)
$query3 = @{
    question = "Nhà máy xử lý chất thải cần giấy phép gì?"
    detailLevel = "detailed"
} | ConvertTo-Json

$resp3 = Invoke-RestMethod -Uri "http://localhost:8085/api/ai/query" `
    -Method Post `
    -ContentType "application/json" `
    -Body $query3

Write-Host "DETAILED ANSWER:`n$($resp3.answer)`n"
```

### 3. Verify Markdown Format:

Tất cả 3 response đều chứa Markdown format, có thể render bằng UI library.

---

## ✅ Checklist

- [ ] Java 17+ installed
- [ ] Docker installed & running
- [ ] MongoDB container started (`docker ps | grep mongodb`)
- [ ] Elasticsearch container started (`docker ps | grep elasticsearch`)
- [ ] Elasticsearch index created (`legal_vectors`)
- [ ] OpenAI API key configured in `application.yml`
- [ ] Project built successfully (`mvn clean package -DskipTests`)
- [ ] Server running on port 8085
- [ ] Documents uploaded via `/api/documents`
- [ ] Query tested via `/api/ai/query` with all 3 detail levels
- [ ] Markdown response rendering works in UI

---

## 📚 Additional Resources

- [Markdown Syntax Guide](https://www.markdownguide.org/)
- [React Markdown](https://github.com/remarkjs/react-markdown)
- [Marked.js](https://marked.js.org/)
- [OpenAI API Docs](https://platform.openai.com/docs/)
- [Elasticsearch Docs](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

---

**Good luck! 🚀**

