# Frontend Test Guide - Payment Admin Dashboard

Base URL:

```text
http://localhost:8085
```

Swagger:

```text
http://localhost:8085/swagger-ui/index.html
```

## 1. Flow Can Test

```text
User closes chat
  -> Backend auto creates Contract
  -> FE creates Payment request
  -> Admin dashboard shows pending payment
  -> SYSTEM_ACCOUNT confirms or rejects payment
  -> Confirmed payment activates Contract
  -> ChatRoom becomes PAYMENT_DONE
  -> ConsultationRequest becomes COMPLETED
```

## 2. Close Chat And Auto Create Contract

Endpoint:

```http
POST /api/chat/rooms/{chatRoomId}/close
```

Body:

```json
{
  "chatRoomId": "CHAT_ROOM_ID",
  "userId": "USER_ID",
  "userRating": 5,
  "userFeedback": "Tu van tot",
  "dealSummary": "Tu van phap ly",
  "generateContract": true
}
```

Expected response fields:

```json
{
  "chatRoomId": "CHAT_ROOM_ID",
  "status": "CONTRACT_CREATED",
  "contractId": "CONTRACT_ID",
  "contractGenerated": true,
  "paymentRequired": true,
  "contractStatus": "PENDING_PAYMENT"
}
```

Save `contractId` for payment testing.

## 3. Create Payment Request

Endpoint:

```http
POST /api/payment/create
```

Body:

```json
{
  "contractId": "CONTRACT_ID",
  "method": "BANKING"
}
```

Allowed methods:

```text
BANKING
MOMO
VNPAY
```

Expected response:

```json
{
  "id": "PAYMENT_ID",
  "contractId": "CONTRACT_ID",
  "status": "PENDING_CONFIRMATION",
  "transactionId": "TXN-..."
}
```

Save `paymentId`.

## 3.1. Legal Contract Form Generated From Chat

After the contract is created, backend also generates a legal contract form from chat messages.

Get form by contract:

```http
GET /api/legal/form/contract/{contractId}
```

Manual generate or regenerate lookup:

```http
GET /api/legal/form/generate/{chatRoomId}?contractId=CONTRACT_ID
```

Expected response fields:

```json
{
  "id": "LEGAL_FORM_ID",
  "contractId": "CONTRACT_ID",
  "chatRoomId": "CHAT_ROOM_ID",
  "consultationRequestId": "CONSULTATION_REQUEST_ID",
  "formType": "CHEMICAL_IMPORT_CONSULTING",
  "title": "Hop dong tu van nhap khau hoa chat",
  "content": "# Hop dong tu van ...",
  "fields": [
    {
      "fieldName": "organizationName",
      "displayLabel": "Ten cong ty",
      "autoFillSource": "user.organizationName",
      "defaultValue": "..."
    }
  ],
  "status": "GENERATED"
}
```

Update review/sign status:

```http
POST /api/legal/form/{formId}/status?status=REVIEWED
POST /api/legal/form/{formId}/status?status=APPROVED
POST /api/legal/form/{formId}/status?status=SIGNED
```

Allowed form statuses:

```text
GENERATED
REVIEWED
APPROVED
SIGNED
```

## 3.2. Export Contract PDF

Endpoint:

```http
GET /api/contract/pdf/export/{contractId}
```

Expected behavior:

```text
Browser downloads contract-CONTRACT_ID.pdf
Content-Type: application/pdf
PDF is stored in GridFS and Contract.pdfFileId is updated
Contract.status = READY_FOR_SIGN
```

FE can use this as a normal download link:

```text
http://localhost:8085/api/contract/pdf/export/CONTRACT_ID
```

After PDF has already been generated, FE can download the stored artifact:

```http
GET /api/contract/pdf/{contractId}
```

## 3.3. E-Sign Contract

Endpoint:

```http
POST /api/contract/sign
```

Body:

```json
{
  "contractId": "CONTRACT_ID",
  "userId": "USER_ID",
  "signerName": "Nguyen Van A",
  "signatureImage": "data:image/png;base64,iVBORw0KGgoAAA...",
  "ipAddress": "127.0.0.1"
}
```

Expected response:

```json
{
  "id": "SIGNATURE_ID",
  "contractId": "CONTRACT_ID",
  "userId": "USER_ID",
  "signerName": "Nguyen Van A",
  "signatureFileId": "GRIDFS_FILE_ID",
  "signatureHash": "SHA_256_HASH",
  "signedAt": "2026-05-21T09:40:00"
}
```

Expected backend side effects:

```text
Contract.signed = true
Contract.signedAt = signedAt
Contract.signedByUserId = USER_ID
Contract.status = SIGNED
LegalContractForm.status = SIGNED
Signature saved to contract_signatures
Signature image/base64 is not stored directly in DB
Signature bytes are stored in GridFS
```

## 4. Admin Dashboard - Pending Payments

Endpoint:

```http
GET /api/admin/dashboard/payment/pending
```

Expected item:

```json
{
  "paymentId": "PAYMENT_ID",
  "contractId": "CONTRACT_ID",
  "userId": "USER_ID",
  "consultantId": "CONSULTANT_ID",
  "contractTitle": "Legal consultation - ...",
  "status": "PENDING_CONFIRMATION",
  "createdAt": "2026-05-21T09:30:00",
  "confirmedAt": null,
  "confirmedBy": null
}
```

FE should show these rows in the admin payment queue.

## 5. Admin Confirm Payment

Endpoint:

```http
POST /api/admin/dashboard/payment/confirm/{paymentId}?adminId=SYSTEM_ADMIN_ID
```

Example:

```http
POST /api/admin/dashboard/payment/confirm/123?adminId=finance_admin_01
```

Expected payment response:

```json
{
  "id": "PAYMENT_ID",
  "contractId": "CONTRACT_ID",
  "status": "CONFIRMED",
  "confirmedBySystemAccountId": "finance_admin_01",
  "confirmedBySystemAccountName": "finance_admin_01",
  "confirmedAt": "2026-05-21T09:35:00"
}
```

Expected backend side effects:

```text
Contract.status = PAID
Contract.paymentStatus = CONFIRMED
ChatRoom.status = PAYMENT_DONE
ConsultationRequest.status = PAID
Audit log saved to payment_confirm_logs
```

## 6. Admin Reject Payment

Endpoint:

```http
POST /api/admin/dashboard/payment/reject/{paymentId}?adminId=SYSTEM_ADMIN_ID&reason=REASON
```

Example:

```http
POST /api/admin/dashboard/payment/reject/123?adminId=finance_admin_01&reason=Invalid transfer proof
```

Expected response:

```json
{
  "id": "PAYMENT_ID",
  "contractId": "CONTRACT_ID",
  "status": "REJECTED",
  "confirmedBySystemAccountId": "finance_admin_01",
  "rejectionReason": "Invalid transfer proof",
  "rejectedAt": "2026-05-21T09:36:00"
}
```

FE should remove rejected payments from the pending queue after refresh.

## 7. Admin KPI

Total revenue:

```http
GET /api/admin/dashboard/revenue
```

Response:

```json
100.0
```

Current demo logic counts each confirmed payment as `100.0`.

Completed contracts count:

```http
GET /api/admin/dashboard/contracts/completed
```

Response:

```json
1
```

Current demo logic counts confirmed payments.

## 8. Direct Contract APIs

Get contract detail:

```http
GET /api/contract/{contractId}
```

Manual contract creation from chat:

```http
POST /api/contract/create/{chatRoomId}
```

Manual admin payment confirm through contract endpoint:

```http
POST /api/contract/pay/{contractId}
```

Body:

```json
{
  "paymentId": "PAYMENT_ID",
  "systemAccountId": "finance_admin_01",
  "systemAccountName": "Finance Admin"
}
```

## 9. Important FE Rules

- Do not call `POST /api/payment/confirm` for admin confirmation. That endpoint is intentionally blocked and returns an error message.
- Use `GET /api/admin/dashboard/payment/pending` to populate the admin payment queue.
- Use `POST /api/admin/dashboard/payment/confirm/{paymentId}` for confirm.
- Use `POST /api/admin/dashboard/payment/reject/{paymentId}` for reject.
- Refresh pending list after confirm or reject.
- Treat `PENDING_CONFIRMATION`, `CONFIRMED`, and `REJECTED` as the main payment states in UI.

## 10. Minimal FE Screens

Admin payment dashboard:

```text
Columns:
- Payment ID
- Contract title
- User ID
- Consultant ID
- Status
- Created at
- Confirm button
- Reject button
```

Admin KPI cards:

```text
- Total revenue
- Completed contracts
- Pending payments count
```

Payment detail modal:

```text
- Payment ID
- Contract ID
- Contract title
- User ID
- Consultant ID
- Status
- Created at
- Confirmed at
- Confirmed by
- Rejection reason
```
