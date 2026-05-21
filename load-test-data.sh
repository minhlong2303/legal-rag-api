#!/bin/bash
# MongoDB Test Data Loader - Biểu Mẫu Hóa Chất

MONGO_URI="mongodb://localhost:27017/rag_api"

echo "Loading test data into MongoDB..."

# Insert form templates
mongosh "$MONGO_URI" << 'EOF'

// Drop existing data (optional)
db.form_templates.deleteMany({})
db.user_profiles.deleteMany({})

// Insert Form Templates
db.form_templates.insertMany([
  {
    _id: "form-02a-import-banned-chemicals",
    formCode: "02a",
    formName: "Văn bản đề nghị cấp Giấy phép nhập khẩu chất thải nguy hại",
    description: "Form để cấp phép nhập khẩu các chất thải nguy hại",
    formType: "IMPORT_PERMIT",
    categories: ["import", "chemical", "permit", "environment"],
    keywords: ["nhập khẩu", "hóa chất", "cấm", "giấy phép", "chất nguy hại", "nhập", "khẩu"],
    active: true,
    fields: [
      {
        fieldId: "organizationName",
        displayLabel: "Tên đơn vị",
        fieldType: "TEXT",
        placeholder: "Công ty ABC Ltd",
        autoFillSource: "user.organizationName",
        required: true,
        notes: "Tên chính thức của tổ chức"
      },
      {
        fieldId: "organizationPhone",
        displayLabel: "Điện thoại đơn vị",
        fieldType: "TEXT",
        autoFillSource: "user.organizationPhone",
        required: true
      },
      {
        fieldId: "organizationAddress",
        displayLabel: "Địa chỉ đơn vị",
        fieldType: "TEXT",
        autoFillSource: "user.organizationAddress",
        required: true
      },
      {
        fieldId: "contactPersonName",
        displayLabel: "Người liên hệ",
        fieldType: "TEXT",
        autoFillSource: "user.contactPersonName",
        required: true
      },
      {
        fieldId: "chemicalName",
        displayLabel: "Tên hóa chất",
        fieldType: "TEXT",
        placeholder: "Chlorine Gas (Khí Clo)",
        required: true,
        suggestions: ["Chlorine Gas", "Ammonia", "Hydrogen Peroxide", "Sodium Hydroxide"]
      },
      {
        fieldId: "casNumber",
        displayLabel: "Số CAS",
        fieldType: "TEXT",
        placeholder: "7782-50-5",
        required: true,
        notes: "Chemical Abstracts Service number"
      },
      {
        fieldId: "quantity",
        displayLabel: "Khối lượng",
        fieldType: "NUMBER",
        required: true
      },
      {
        fieldId: "quantityUnit",
        displayLabel: "Đơn vị",
        fieldType: "SELECT",
        suggestions: ["kg", "tấn", "lít", "m³"],
        required: true
      },
      {
        fieldId: "importPurpose",
        displayLabel: "Mục đích nhập khẩu",
        fieldType: "TEXTAREA",
        required: true
      },
      {
        fieldId: "permissionNumber",
        displayLabel: "Số giấy phép",
        fieldType: "TEXT",
        required: false
      },
      {
        fieldId: "issueDate",
        displayLabel: "Ngày cấp",
        fieldType: "DATE",
        required: false
      },
      {
        fieldId: "expiryDate",
        displayLabel: "Ngày hết hạn",
        fieldType: "DATE",
        required: false
      }
    ]
  },
  {
    _id: "form-02b-export-banned-chemicals",
    formCode: "02b",
    formName: "Văn bản xác nhận xuất khẩu chất thải nguy hại",
    description: "Form để xác nhận xuất khẩu các chất thải nguy hại",
    formType: "EXPORT_PERMIT",
    categories: ["export", "chemical", "permit", "environment"],
    keywords: ["xuất khẩu", "hóa chất", "chất nguy hại", "xuất", "khẩu"],
    active: true,
    fields: [
      {
        fieldId: "organizationName",
        displayLabel: "Tên đơn vị",
        fieldType: "TEXT",
        autoFillSource: "user.organizationName",
        required: true
      },
      {
        fieldId: "chemicalName",
        displayLabel: "Tên hóa chất",
        fieldType: "TEXT",
        required: true
      },
      {
        fieldId: "quantity",
        displayLabel: "Khối lượng",
        fieldType: "NUMBER",
        required: true
      },
      {
        fieldId: "destinationCountry",
        displayLabel: "Quốc gia nhập khẩu",
        fieldType: "TEXT",
        required: true
      }
    ]
  },
  {
    _id: "form-03-chemical-storage",
    formCode: "03",
    formName: "Văn bản khai báo kho lưu trữ hóa chất",
    description: "Form khai báo thông tin kho lưu trữ hóa chất",
    formType: "STORAGE_DECLARATION",
    categories: ["storage", "chemical", "warehouse", "environment"],
    keywords: ["kho", "lưu trữ", "hóa chất", "warehouse", "storage"],
    active: true,
    fields: [
      {
        fieldId: "organizationName",
        displayLabel: "Tên đơn vị",
        fieldType: "TEXT",
        autoFillSource: "user.organizationName",
        required: true
      },
      {
        fieldId: "warehouseAddress",
        displayLabel: "Địa chỉ kho",
        fieldType: "TEXT",
        autoFillSource: "user.warehouseAddress",
        required: true
      },
      {
        fieldId: "storageCapacity",
        displayLabel: "Dung tích kho (m³)",
        fieldType: "NUMBER",
        required: true
      },
      {
        fieldId: "chemicalList",
        displayLabel: "Danh sách hóa chất lưu trữ",
        fieldType: "TEXTAREA",
        required: true
      }
    ]
  }
])

// Insert test user profile
db.user_profiles.insertOne({
  _id: "user-test-001",
  userId: "user-test-001",
  organizationName: "Công ty Hóa chất Việt Nam",
  organizationPhone: "0834567890",
  organizationAddress: "123 Nguyễn Trãi, Hà Nội",
  warehouseAddress: "Khu công nghiệp Thăng Long, Hà Nội",
  contactPersonName: "Nguyễn Văn An",
  contactPersonEmail: "an@company.vn",
  contactPersonPhone: "0912345678",
  completionStatus: true,
  createdAt: new Date(),
  updatedAt: new Date()
})

console.log("✓ Test data loaded successfully!")
console.log("Forms: 3 templates")
console.log("Users: 1 profile")

EOF

echo ""
echo "Test data loading complete!"
echo "Ready to test API endpoints."

