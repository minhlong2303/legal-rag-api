package com.ragapi.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Service
public class PdfStorageService {

    private final GridFsTemplate legalDocumentGridFsTemplate;
    private final GridFsTemplate contractGridFsTemplate;
    private final GridFsTemplate signatureGridFsTemplate;

    public PdfStorageService(
            @Qualifier("legalDocumentGridFsTemplate")
            GridFsTemplate legalDocumentGridFsTemplate,
            @Qualifier("contractGridFsTemplate")
            GridFsTemplate contractGridFsTemplate,
            @Qualifier("signatureGridFsTemplate")
            GridFsTemplate signatureGridFsTemplate
    ) {
        this.legalDocumentGridFsTemplate = legalDocumentGridFsTemplate;
        this.contractGridFsTemplate = contractGridFsTemplate;
        this.signatureGridFsTemplate = signatureGridFsTemplate;
    }

    public String store(byte[] pdfBytes, String fileName, String documentId) {

        org.bson.Document metadata = new org.bson.Document()
                .append("documentId", documentId)
                .append("contentType", "application/pdf");

        ObjectId fileId = legalDocumentGridFsTemplate.store(
                new ByteArrayInputStream(pdfBytes),
                fileName,
                "application/pdf",
                metadata
        );

        log.info(
                "PDF saved to MongoDB GridFS bucket 'legal_pdfs': id={}, documentId={}, size={} bytes",
                fileId,
                documentId,
                pdfBytes.length
        );

        return fileId.toHexString();
    }

    public GridFsResource loadByDocumentId(String documentId) throws IOException {

        GridFSFile file = legalDocumentGridFsTemplate.findOne(
                Query.query(Criteria.where("metadata.documentId").is(documentId))
        );

        if (file == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy file PDF trong MongoDB cho văn bản này"
            );
        }

        return legalDocumentGridFsTemplate.getResource(file);
    }

    public GridFsResource loadByFileId(String pdfFileId) throws IOException {

        GridFSFile file = legalDocumentGridFsTemplate.findOne(
                Query.query(Criteria.where("_id").is(new ObjectId(pdfFileId)))
        );

        if (file == null) {
            throw new IllegalArgumentException("Không tìm thấy file PDF với id: " + pdfFileId);
        }

        return legalDocumentGridFsTemplate.getResource(file);
    }

    public String storePdf(byte[] pdfBytes, String contractId) {
        org.bson.Document metadata = new org.bson.Document()
                .append("contractId", contractId)
                .append("type", "CONTRACT_PDF")
                .append("contentType", "application/pdf");

        ObjectId fileId = contractGridFsTemplate.store(
                new ByteArrayInputStream(pdfBytes),
                "contract_" + contractId + ".pdf",
                "application/pdf",
                metadata
        );

        log.info(
                "Contract PDF saved to GridFS: id={}, contractId={}, size={} bytes",
                fileId,
                contractId,
                pdfBytes.length
        );

        return fileId.toHexString();
    }

    public GridFsResource loadContractPdfByContractId(String contractId) throws IOException {
        GridFSFile file = contractGridFsTemplate.findOne(
                Query.query(Criteria.where("metadata.contractId").is(contractId)
                        .and("metadata.type").is("CONTRACT_PDF"))
        );

        if (file == null) {
            throw new IllegalArgumentException("Contract PDF not found for contractId: " + contractId);
        }

        return contractGridFsTemplate.getResource(file);
    }

    public GridFsResource loadContractPdfByFileId(String pdfFileId) throws IOException {
        GridFSFile file = contractGridFsTemplate.findOne(
                Query.query(Criteria.where("_id").is(new ObjectId(pdfFileId)))
        );

        if (file == null) {
            throw new IllegalArgumentException("Contract PDF not found with id: " + pdfFileId);
        }

        return contractGridFsTemplate.getResource(file);
    }

    public String storeSignatureFile(byte[] signatureBytes, String contractId, String userId) {
        org.bson.Document metadata = new org.bson.Document()
                .append("contractId", contractId)
                .append("userId", userId)
                .append("type", "CONTRACT_SIGNATURE")
                .append("contentType", "application/octet-stream");

        ObjectId fileId = signatureGridFsTemplate.store(
                new ByteArrayInputStream(signatureBytes),
                "signature_" + contractId + "_" + userId,
                "application/octet-stream",
                metadata
        );

        log.info(
                "Contract signature saved to GridFS: id={}, contractId={}, userId={}, size={} bytes",
                fileId,
                contractId,
                userId,
                signatureBytes.length
        );

        return fileId.toHexString();
    }
}
