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
3. **KNN Search** → Elasticsearch (k=3, top 3 chunks)
4. **Build Prompt** → Combine context + question
5. **Generate Answer** → OpenAI GPT-4.1-mini
6. **Return Result** → AI-generated answer

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
| `401 Unauthorized` | Check OpenAI API key in `application.yml` |
| `"Không tìm thấy thông tin phù hợp"` | Upload document first, then query |
| `500 Internal Server Error` | Check logs: `mvn spring-boot:run 2>&1 \| tee logs.txt` |

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
