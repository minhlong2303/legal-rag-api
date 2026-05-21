# RAG_API - Hệ thống RAG Thực Tế cho Pháp Luật Môi Trường TP.HCM

## 📋 Mô Tả

Dự án **RAG_API** là một microservice Spring Boot thực hiện **Retrieval-Augmented Generation (RAG)** 100% thực tế:
- ✅ Upload tài liệu pháp luật
- ✅ Lưu trữ trong MongoDB
- ✅ Chunk text và generate embeddings
- ✅ Index vector vào Elasticsearch
- ✅ Semantic search thực sự
- ✅ AI-generated answer từ GPT-4.1-mini

## 🏗️ Architecture

```
┌─────────────────┐
│  RAG_API Server │ (Port 8085)
└────────┬────────┘
         │
    ┌────┴─────┬──────────┬─────────────┐
    │           │          │             │
┌───▼────┐ ┌───▼──┐ ┌──────▼─────┐ ┌───▼────┐
│ MongoDB │ │Elastic   │ OpenAI    │
│ (27017) │ │(9200)    │ (API)     │
└────────┘ └────────┘ └───────────┘
```

## 📖 Documentation Index

**For Frontend Team:**
- 🎨 **[FRONTEND_UI_UX_COMPLETE.md](./FRONTEND_UI_UX_COMPLETE.md)** - Complete UI/UX guide with React components (copy-paste ready)

**For Backend/Implementation:**
- 📘 **[START_HERE.md](./START_HERE.md)** - Getting started guide
- 🏛️ **[CONSULTANT_SYSTEM_GUIDE.md](./CONSULTANT_SYSTEM_GUIDE.md)** - Consultant system architecture & design
- 💻 **[CONSULTANT_IMPLEMENTATION_GUIDE.md](./CONSULTANT_IMPLEMENTATION_GUIDE.md)** - Backend implementation details
- 📋 **[CONSULTANT_API_DOCUMENTATION.md](./CONSULTANT_API_DOCUMENTATION.md)** - Complete API reference
- 📊 **[FORM_SYSTEM_GUIDE.md](./FORM_SYSTEM_GUIDE.md)** - Form system documentation
- 📤 **[CONSULTANT_IMPORT_GUIDE.md](./CONSULTANT_IMPORT_GUIDE.md)** - Excel import feature guide

**For DevOps/Deployment:**
- 🚀 **[DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md)** - Production deployment instructions

**For Troubleshooting:**
- 🆘 **[TROUBLESHOOTING.md](./TROUBLESHOOTING.md)** - Common issues and solutions

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven
- Docker (optional, but recommended)

### 1. Chạy MongoDB
```bash
docker run -d --name mongodb -p 27017:27017 mongo:latest
```

### 2. Chạy Elasticsearch
```bash
docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" elasticsearch:8.13.0
```

### 3. Tạo Elasticsearch Index
```bash
# Mở PowerShell hoặc terminal và chạy:
$body = @{mappings=@{properties=@{documentId=@{type="keyword"};content=@{type="text"};vector=@{type="dense_vector";dims=1536;index=$true;similarity="cosine"}}}} | ConvertTo-Json -Depth 10
Invoke-WebRequest -Uri "http://localhost:9200/legal_vectors" -Method PUT -Body $body -ContentType "application/json"
```

### 4. Cấu hình OpenAI API Key
Edit file `src/main/resources/application.yml`:
```yaml
openai:
  api-key: sk-proj-YOUR_ACTUAL_API_KEY
```

### 5. Build & Run
```bash
cd D:\RAG_API\RAG_API
mvn clean package -DskipTests
java -jar target/RAG_API-1.0.0.jar
```

### 6. Test API
Mở Swagger UI: `http://localhost:8085/swagger-ui.html`

## 📚 API Endpoints

### Upload Document (Ingestion)
```http
POST /api/documents
Content-Type: application/json

{
  "title": "Waste Treatment Regulation",
  "category": "environment",
  "content": "Nhà máy xử lý chất thải tại TP.HCM phải có giấy phép môi trường và giấy phép xây dựng theo quy định hiện hành..."
}
```

**Response (200 OK):**
```
"Document indexed successfully"
```

### Query AI (RAG)
```http
POST /api/ai/query
Content-Type: application/json

{
  "question": "Nhà máy xử lý chất thải cần giấy phép gì?"
}
```

**Response (200 OK):**
```json
{
  "answer": "Nhà máy xử lý chất thải tại TP.HCM phải có giấy phép môi trường và giấy phép xây dựng theo quy định hiện hành."
}
```

### Query AI with Detail Level (Support Markdown Format)
```http
POST /api/ai/query
Content-Type: application/json

{
  "question": "Nhà máy xử lý chất thải cần giấy phép gì?",
  "detailLevel": "detailed"
}
```

**Response (200 OK):**
```json
{
  "answer": "## Câu trả lời\n\nNhà máy xử lý chất thải **bắt buộc phải có giấy phép môi trường**, và **nếu xử lý chất thải nguy hại cần bổ sung giấy phép xử lý CTNH**.\n\n### Giải thích chi tiết\n\n* **Giấy phép môi trường**: Giấy phép tích hợp các loại giấy phép thành phần..."
}
```

**Detail Levels:**
| Level | Định dạng | Kích thước |
|-------|-----------|-----------|
| `brief` | Ngắn gọn (heading + bold + trích dẫn) | ~200 từ |
| `normal` (default) | Vừa phải (giải thích + điều kiện + trích dẫn) | ~400 từ |
| `detailed` | Chi tiết (giải thích sâu + ví dụ + ngoại lệ) | ~800+ từ |

**Response Format (Markdown):**
```markdown
## Câu trả lời
...

### Giải thích
* **Điều khoản 1**: ...
* **Điều khoản 2**: ...

### Điều kiện áp dụng
1. Điều kiện thứ nhất
2. Điều kiện thứ hai

### Trích dẫn pháp luật
* "Điều ... - Khoản ..."
```

> **💡 Tip**: Response field `answer` chứa Markdown format. Sử dụng React Markdown, Marked.js, hoặc markdown libraries khác để render đẹp trên UI.

## 🔄 RAG Pipeline

### Ingestion Flow
1. **Upload Document** → `POST /api/documents`
2. **Save to MongoDB** → `LegalDocument` collection
3. **Chunk Text** → 500 ký tự/chunk
4. **Generate Embeddings** → OpenAI `text-embedding-3-small`
5. **Index to Elasticsearch** → `legal_vectors` index

### Retrieval Flow
1. **User Question** → `POST /api/ai/query`
2. **Embed Question** → OpenAI embedding
3. **KNN Search** → Elasticsearch (k=5, top 5 chunks with improved relevance)
4. **Build Prompt** → Combine context + question + format instructions
5. **Generate Answer** → OpenAI GPT-4.1-mini (with Markdown format)
6. **Return Result** → AI-generated answer with structured format

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.2.5
- **Database**: MongoDB (Document storage)
- **Vector DB**: Elasticsearch 8.13.0 (Vector search)
- **AI/LLM**: OpenAI (Embedding + Chat)
- **LangChain**: LangChain4j 0.32.0 (AI integration)
- **API Doc**: Swagger/OpenAPI
- **Build**: Maven

## 📊 Project Structure

```
RAG_API/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/ragapi/
│   │   ├── RagApiApplication.java
│   │   ├── config/
│   │   │   ├── ElasticConfig.java
│   │   │   └── OpenAIConfig.java
│   │   ├── controller/
│   │   │   └── RagController.java
│   │   ├── service/
│   │   │   ├── DocumentIngestionService.java
│   │   │   ├── ElasticVectorService.java
│   │   │   ├── EmbeddingService.java
│   │   │   ├── RagService.java
│   │   │   └── TextChunkingService.java
│   │   ├── entity/
│   │   │   └── LegalDocument.java
│   │   └── repository/
│   │       └── LegalDocumentRepository.java
│   └── resources/
│       └── application.yml
```

## ❌ Troubleshooting

| Problem | Solution |
|---------|----------|
| `Connection refused localhost:27017` | Run MongoDB: `docker run -d -p 27017:27017 mongo:latest` |
| `Connection refused localhost:9200` | Run Elasticsearch: `docker run -d -p 9200:9200 -e "discovery.type=single-node" elasticsearch:8.13.0` |
| `{"error": "no such index"}` | Create index `legal_vectors` in Elasticsearch |
| `❌ Lỗi máy chủ: Không thể gọi tới dịch vụ LLM` | **See QUICK_FIX.md** or run `.\diagnose-llm.ps1` |
| `401 Unauthorized` | Check OpenRouter API key in `application.yml` |
| `"Không tìm thấy thông tin phù hợp"` | Upload document first, then query |
| `500 Internal Server Error` | **See TROUBLESHOOTING.md** for log analysis |

**For detailed debugging:**
- Quick fix (30 sec): `QUICK_FIX.md`
- Full troubleshooting: `TROUBLESHOOTING.md`
- Run diagnostics: `.\diagnose-llm.ps1`
- Check logs: Look for "========== LLM API ERROR ==========" section in server output

## 📝 Logs Location

Khi chạy server, logs sẽ hiển thị:
- Document ingestion progress
- Embedding generation
- Vector indexing status
- Elasticsearch search results
- AI generation progress

## 🧪 Test Scripts

Dùng files batch có sẵn:
- `start-rag.bat` - Build & run server
- `test-and-run.bat` - Check dependencies, build, run
- `run.bat` - Simple run

## 📄 License

MIT

## 📧 Contact

For issues or questions about this RAG implementation, check logs and error messages for details.

---

**Remember**: This is a REAL RAG system, not a mock. Every component is functional and production-ready!
