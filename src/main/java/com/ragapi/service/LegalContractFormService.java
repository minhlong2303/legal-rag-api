package com.ragapi.service;

import com.ragapi.entity.ChatMessage;
import com.ragapi.entity.ChatRoom;
import com.ragapi.entity.FormField;
import com.ragapi.entity.LegalContractForm;
import com.ragapi.entity.LegalContractFormStatus;
import com.ragapi.entity.LegalContractFormType;
import com.ragapi.entity.UserProfile;
import com.ragapi.repository.ChatMessageRepository;
import com.ragapi.repository.ChatRoomRepository;
import com.ragapi.repository.LegalContractFormRepository;
import com.ragapi.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LegalContractFormService {

    private final LegalContractFormRepository legalContractFormRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserProfileRepository userProfileRepository;

    public LegalContractForm generateFromChat(String chatRoomId, String contractId) {
        return legalContractFormRepository.findByContractId(contractId)
                .orElseGet(() -> generateNewFromChat(chatRoomId, contractId));
    }

    public LegalContractForm getByContractId(String contractId) {
        return legalContractFormRepository.findByContractId(contractId)
                .orElseThrow(() -> new RuntimeException("Legal contract form not found"));
    }

    public LegalContractForm getById(String formId) {
        return legalContractFormRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Legal contract form not found"));
    }

    public LegalContractForm updateStatus(String formId, LegalContractFormStatus status) {
        LegalContractForm form = getById(formId);
        form.setStatus(status);
        form.setUpdatedAt(LocalDateTime.now());
        return legalContractFormRepository.save(form);
    }

    private LegalContractForm generateNewFromChat(String chatRoomId, String contractId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
        String combinedText = messages.stream()
                .map(ChatMessage::getContent)
                .filter(content -> content != null && !content.isBlank())
                .collect(Collectors.joining(" "));

        LegalContractFormType formType = detectFormType(combinedText);
        UserProfile profile = userProfileRepository.findByUserId(room.getUserId())
                .orElse(null);

        LegalContractForm form = LegalContractForm.builder()
                .id(UUID.randomUUID().toString())
                .contractId(contractId)
                .chatRoomId(chatRoomId)
                .consultationRequestId(room.getConsultationRequestId())
                .formType(formType)
                .title(generateTitle(formType))
                .content(generateContractContent(formType, combinedText, room, profile))
                .fields(buildAutoFillFields(profile))
                .status(LegalContractFormStatus.GENERATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return legalContractFormRepository.save(form);
    }

    private LegalContractFormType detectFormType(String text) {
        String normalized = text != null ? text.toLowerCase() : "";

        if (normalized.contains("nhap khau")
                || normalized.contains("nhập khẩu")) {
            return LegalContractFormType.CHEMICAL_IMPORT_CONSULTING;
        }

        if (normalized.contains("xuat khau")
                || normalized.contains("xuất khẩu")) {
            return LegalContractFormType.CHEMICAL_EXPORT_CONSULTING;
        }

        if (normalized.contains("luu tru")
                || normalized.contains("lưu trữ")
                || normalized.contains("kho")) {
            return LegalContractFormType.CHEMICAL_STORAGE_COMPLIANCE;
        }

        if (normalized.contains("giay phep")
                || normalized.contains("giấy phép")
                || normalized.contains("san xuat")
                || normalized.contains("sản xuất")) {
            return LegalContractFormType.CHEMICAL_PRODUCTION_LICENSE;
        }

        if (normalized.contains("an toan")
                || normalized.contains("an toàn")
                || normalized.contains("audit")
                || normalized.contains("kiem tra")
                || normalized.contains("kiểm tra")) {
            return LegalContractFormType.CHEMICAL_SAFETY_AUDIT;
        }

        return LegalContractFormType.GENERAL_LEGAL_CONSULTING;
    }

    private String generateContractContent(
            LegalContractFormType type,
            String chatContext,
            ChatRoom room,
            UserProfile profile
    ) {
        String customerName = valueOrDefault(
                profile != null ? profile.getOrganizationName() : null,
                room.getUserName()
        );
        String customerAddress = valueOrDefault(
                profile != null ? profile.getOrganizationAddress() : null,
                "Chua cap nhat"
        );
        String representative = valueOrDefault(
                profile != null ? profile.getLegalRepresentativeName() : null,
                room.getUserName()
        );
        String taxCode = valueOrDefault(
                profile != null ? profile.getBusinessRegistrationNumber() : null,
                "Chua cap nhat"
        );
        String warehouse = valueOrDefault(
                profile != null ? profile.getWarehouseAddress() : null,
                "Chua cap nhat"
        );

        String header = """
                # %s

                ## 1. Ben A - Khach hang
                - Ten don vi: %s
                - Dia chi: %s
                - Nguoi dai dien: %s
                - Ma so thue/DKKD: %s
                - Kho luu tru hoa chat: %s

                ## 2. Ben B - Tu van vien
                - Ten tu van vien: %s
                - Email: %s

                ## 3. Can cu hinh thanh hop dong
                Hop dong duoc sinh tu noi dung trao doi giua khach hang va tu van vien tren he thong LegalTech.

                """.formatted(
                generateTitle(type),
                customerName,
                customerAddress,
                representative,
                taxCode,
                warehouse,
                valueOrDefault(room.getConsultantName(), "Chua cap nhat"),
                valueOrDefault(room.getConsultantEmail(), "Chua cap nhat")
        );

        return header + switch (type) {
            case CHEMICAL_IMPORT_CONSULTING -> """
                    ## 4. Pham vi tu van nhap khau hoa chat
                    - Tu van dieu kien nhap khau hoa chat.
                    - Ra soat ho so phap ly lien quan.
                    - Canh bao rui ro ve danh muc hoa chat han che/cam.
                    - Huong dan chuan bi tai lieu lam viec voi co quan quan ly.

                    ## 5. Noi dung tu van chi tiet
                    """ + chatContext;
            case CHEMICAL_EXPORT_CONSULTING -> """
                    ## 4. Pham vi tu van xuat khau hoa chat
                    - Tu van dieu kien xuat khau hoa chat.
                    - Ra soat chung tu va yeu cau khai bao.
                    - Huong dan quan tri rui ro phap ly trong giao dich xuat khau.

                    ## 5. Noi dung tu van chi tiet
                    """ + chatContext;
            case CHEMICAL_STORAGE_COMPLIANCE -> """
                    ## 4. Pham vi tu van luu tru hoa chat
                    - Tu van quy dinh an toan kho hoa chat.
                    - Ra soat dieu kien luu tru va bien phap phong ngua rui ro.
                    - De xuat ho so/chung tu can bo sung.

                    ## 5. Noi dung tu van chi tiet
                    """ + chatContext;
            case CHEMICAL_PRODUCTION_LICENSE -> """
                    ## 4. Pham vi tu van giay phep san xuat hoa chat
                    - Tu van dieu kien xin/cap lai/dieu chinh giay phep.
                    - Huong dan thanh phan ho so va quy trinh nop.
                    - Ra soat tinh day du cua tai lieu phap ly.

                    ## 5. Noi dung tu van chi tiet
                    """ + chatContext;
            case CHEMICAL_SAFETY_AUDIT -> """
                    ## 4. Pham vi danh gia an toan hoa chat
                    - Ra soat nghia vu tuan thu an toan hoa chat.
                    - Tu van bien phap khac phuc diem khong phu hop.
                    - De xuat checklist theo doi tuan thu.

                    ## 5. Noi dung tu van chi tiet
                    """ + chatContext;
            case GENERAL_LEGAL_CONSULTING -> """
                    ## 4. Pham vi tu van phap ly chung
                    - Tong hop van de phap ly do khach hang cung cap.
                    - Tu van huong xu ly va tai lieu can chuan bi.

                    ## 5. Noi dung tu van chi tiet
                    """ + chatContext;
        };
    }

    private String generateTitle(LegalContractFormType type) {
        return switch (type) {
            case CHEMICAL_IMPORT_CONSULTING -> "Hop dong tu van nhap khau hoa chat";
            case CHEMICAL_EXPORT_CONSULTING -> "Hop dong tu van xuat khau hoa chat";
            case CHEMICAL_STORAGE_COMPLIANCE -> "Hop dong tu van luu tru hoa chat";
            case CHEMICAL_PRODUCTION_LICENSE -> "Hop dong tu van giay phep san xuat hoa chat";
            case CHEMICAL_SAFETY_AUDIT -> "Hop dong danh gia an toan hoa chat";
            case GENERAL_LEGAL_CONSULTING -> "Hop dong tu van phap ly";
        };
    }

    private List<FormField> buildAutoFillFields(UserProfile profile) {
        List<FormField> fields = new ArrayList<>();

        fields.add(field("organization_name", "organizationName", "Ten cong ty", "user.organizationName",
                profile != null ? profile.getOrganizationName() : null, true, 1));
        fields.add(field("organization_address", "organizationAddress", "Dia chi cong ty", "user.organizationAddress",
                profile != null ? profile.getOrganizationAddress() : null, true, 2));
        fields.add(field("legal_representative_name", "legalRepresentativeName", "Nguoi dai dien",
                "user.legalRepresentativeName", profile != null ? profile.getLegalRepresentativeName() : null, true, 3));
        fields.add(field("business_registration_number", "businessRegistrationNumber", "Ma so thue/DKKD",
                "user.businessRegistrationNumber", profile != null ? profile.getBusinessRegistrationNumber() : null, true, 4));
        fields.add(field("warehouse_address", "warehouseAddress", "Kho luu tru hoa chat",
                "user.warehouseAddress", profile != null ? profile.getWarehouseAddress() : null, false, 5));

        return fields;
    }

    private FormField field(
            String fieldId,
            String fieldName,
            String label,
            String autoFillSource,
            Object defaultValue,
            boolean required,
            int displayOrder
    ) {
        return FormField.builder()
                .fieldId(fieldId)
                .fieldName(fieldName)
                .displayLabel(label)
                .fieldType("TEXT")
                .required(required)
                .autoFillSource(autoFillSource)
                .defaultValue(defaultValue)
                .displayOrder(displayOrder)
                .build();
    }

    private String valueOrDefault(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback != null && !fallback.isBlank() ? fallback : "Chua cap nhat";
        }

        return value;
    }
}
