param(
    [string]$mongoUri = "mongodb://localhost:27017"
)

Write-Host "Loading test data into MongoDB..." -ForegroundColor Cyan

# JavaScript for MongoDB
$jsScript = @"
use rag_api

// Drop existing data
db.form_templates.deleteMany({})
db.user_profiles.deleteMany({})

// Insert Form Templates
db.form_templates.insertMany([
  {
    _id: "form-02a-import-banned-chemicals",
    formCode: "02a",
    formName: "Van ban de nghi cap Giay phep nhap khau chat thai nguy hai",
    description: "Form de cap phep nhap khau cac chat thai nguy hai",
    formType: "IMPORT_PERMIT",
    categories: ["import", "chemical", "permit", "environment"],
    keywords: ["nhap khau", "hoa chat", "cam", "giay phep", "chat nguy hai"],
    active: true,
    fields: [
      {
        fieldId: "organizationName",
        displayLabel: "Ten don vi",
        fieldType: "TEXT",
        placeholder: "Cong ty ABC Ltd",
        autoFillSource: "user.organizationName",
        required: true,
        notes: "Ten chinh thuc cua to chuc"
      },
      {
        fieldId: "organizationPhone",
        displayLabel: "Dien thoai don vi",
        fieldType: "TEXT",
        autoFillSource: "user.organizationPhone",
        required: true
      },
      {
        fieldId: "organizationAddress",
        displayLabel: "Dia chi don vi",
        fieldType: "TEXT",
        autoFillSource: "user.organizationAddress",
        required: true
      },
      {
        fieldId: "contactPersonName",
        displayLabel: "Nguoi lien he",
        fieldType: "TEXT",
        autoFillSource: "user.contactPersonName",
        required: true
      },
      {
        fieldId: "chemicalName",
        displayLabel: "Ten hoa chat",
        fieldType: "TEXT",
        placeholder: "Chlorine Gas",
        required: true,
        suggestions: ["Chlorine Gas", "Ammonia", "Hydrogen Peroxide"]
      },
      {
        fieldId: "casNumber",
        displayLabel: "So CAS",
        fieldType: "TEXT",
        placeholder: "7782-50-5",
        required: true
      },
      {
        fieldId: "quantity",
        displayLabel: "Khoi luong",
        fieldType: "NUMBER",
        required: true
      },
      {
        fieldId: "quantityUnit",
        displayLabel: "Don vi",
        fieldType: "SELECT",
        suggestions: ["kg", "tan", "lit"],
        required: true
      },
      {
        fieldId: "importPurpose",
        displayLabel: "Muc dich nhap khau",
        fieldType: "TEXTAREA",
        required: true
      },
      {
        fieldId: "permissionNumber",
        displayLabel: "So giay phep",
        fieldType: "TEXT",
        required: false
      },
      {
        fieldId: "issueDate",
        displayLabel: "Ngay cap",
        fieldType: "DATE",
        required: false
      },
      {
        fieldId: "expiryDate",
        displayLabel: "Ngay het han",
        fieldType: "DATE",
        required: false
      }
    ]
  },
  {
    _id: "form-02b-export-banned-chemicals",
    formCode: "02b",
    formName: "Van ban xac nhan xuat khau chat thai nguy hai",
    description: "Form de xac nhan xuat khau cac chat thai nguy hai",
    formType: "EXPORT_PERMIT",
    categories: ["export", "chemical", "permit"],
    keywords: ["xuat khau", "hoa chat", "chat nguy hai"],
    active: true,
    fields: [
      {
        fieldId: "organizationName",
        displayLabel: "Ten don vi",
        fieldType: "TEXT",
        autoFillSource: "user.organizationName",
        required: true
      },
      {
        fieldId: "chemicalName",
        displayLabel: "Ten hoa chat",
        fieldType: "TEXT",
        required: true
      },
      {
        fieldId: "quantity",
        displayLabel: "Khoi luong",
        fieldType: "NUMBER",
        required: true
      },
      {
        fieldId: "destinationCountry",
        displayLabel: "Quoc gia nhap khau",
        fieldType: "TEXT",
        required: true
      }
    ]
  },
  {
    _id: "form-03-chemical-storage",
    formCode: "03",
    formName: "Van ban khai bao kho luu tru hoa chat",
    description: "Form khai bao thong tin kho luu tru hoa chat",
    formType: "STORAGE_DECLARATION",
    categories: ["storage", "chemical", "warehouse"],
    keywords: ["kho", "luu tru", "hoa chat", "warehouse"],
    active: true,
    fields: [
      {
        fieldId: "organizationName",
        displayLabel: "Ten don vi",
        fieldType: "TEXT",
        autoFillSource: "user.organizationName",
        required: true
      },
      {
        fieldId: "warehouseAddress",
        displayLabel: "Dia chi kho",
        fieldType: "TEXT",
        autoFillSource: "user.warehouseAddress",
        required: true
      },
      {
        fieldId: "storageCapacity",
        displayLabel: "Dung tich kho",
        fieldType: "NUMBER",
        required: true
      },
      {
        fieldId: "chemicalList",
        displayLabel: "Danh sach hoa chat",
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
  organizationName: "Cong ty Hoa chat Viet Nam",
  organizationPhone: "0834567890",
  organizationAddress: "123 Nguyen Trai, Ha Noi",
  warehouseAddress: "Khu cong nghiep Thang Long",
  contactPersonName: "Nguyen Van An",
  contactPersonEmail: "an@company.vn",
  contactPersonPhone: "0912345678",
  completionStatus: true,
  createdAt: new Date(),
  updatedAt: new Date()
})

print("Test data loaded successfully!")
print("Forms: 3 templates")
print("Users: 1 profile")
"@

# Save script to file
$jsFile = "C:\temp\load-test-data.js"
$jsScript | Out-File -FilePath $jsFile -Encoding UTF8

# Run mongosh
Write-Host "Executing MongoDB commands..." -ForegroundColor Green

try {
    # Try mongosh first (MongoDB 5.0+)
    & mongosh $mongoUri --file $jsFile
} catch {
    try {
        # Fallback to mongo shell (older MongoDB versions)
        & mongo $mongoUri $jsFile
    } catch {
        Write-Host "Error: Neither mongosh nor mongo found." -ForegroundColor Red
        Write-Host "Make sure MongoDB is installed and in PATH" -ForegroundColor Yellow
    }
}

# Cleanup
Remove-Item -Path $jsFile -Force

Write-Host "`nTest data loading complete!" -ForegroundColor Green
Write-Host "Ready to test API endpoints." -ForegroundColor Cyan

