#!/bin/bash
# API Test Examples for RAG Legal Service with Markdown Format

BASE_URL="http://localhost:8085"

echo "================================"
echo "🎯 RAG API - Format Response Test"
echo "================================"
echo ""

# Test 1: BRIEF (Tóm tắt)
echo "1️⃣ TEST BRIEF (Tóm tắt)"
echo "Request:"
echo '  POST /api/ai/query'
echo '  {"question": "Nhà máy xử lý chất thải cần giấy phép gì?", "detailLevel": "brief"}'
echo ""
echo "cURL:"
curl -X POST "$BASE_URL/api/ai/query" \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Nhà máy xử lý chất thải cần giấy phép gì?",
    "detailLevel": "brief"
  }' | jq .

echo ""
echo "---"
echo ""

# Test 2: NORMAL (Vừa phải - mặc định)
echo "2️⃣ TEST NORMAL (Vừa phải)"
echo "Request:"
echo '  POST /api/ai/query'
echo '  {"question": "Nhà máy xử lý chất thải cần giấy phép gì?"}'
echo ""
echo "cURL:"
curl -X POST "$BASE_URL/api/ai/query" \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Nhà máy xử lý chất thải cần giấy phép gì?"
  }' | jq .

echo ""
echo "---"
echo ""

# Test 3: DETAILED (Chi tiết)
echo "3️⃣ TEST DETAILED (Chi tiết)"
echo "Request:"
echo '  POST /api/ai/query'
echo '  {"question": "Nhà máy xử lý chất thải cần giấy phép gì?", "detailLevel": "detailed"}'
echo ""
echo "cURL:"
curl -X POST "$BASE_URL/api/ai/query" \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Nhà máy xử lý chất thải cần giấy phép gì?",
    "detailLevel": "detailed"
  }' | jq .

echo ""
echo "================================"
echo "✅ Test Complete!"
echo "================================"
echo ""
echo "Notes:"
echo "  - Hãy chắc chắn server đang chạy trên port 8085"
echo "  - Response sẽ là JSON với field 'answer' chứa Markdown format"
echo "  - Bạn có thể renders Markdown trong UI bằng markdown libraries (React Markdown, Marked.js, etc.)"
echo ""

