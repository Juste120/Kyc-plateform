package com.edgar.kyc.document.internal;

import com.edgar.kyc.customer.CustomerLookup;
import com.edgar.kyc.document.DocumentDeletedEvent;
import com.edgar.kyc.document.DocumentUploadedEvent;
import com.edgar.kyc.document.internal.response.DocumentResponse;
import com.edgar.kyc.exception.BusinessException;
import com.edgar.kyc.exception.ResourceNotFoundException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of document management service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final CustomerLookup customerLookup;
    private final DocumentMapper documentMapper;
    private final MinioClient minioClient;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024L; // 20 MB
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "application/pdf"
    );

    @Override
    public DocumentResponse uploadDocument(String email, MultipartFile file, String documentType) {
        log.info("Uploading document for user: {}", email);

        if (file.isEmpty()) {
            throw new BusinessException("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("File size exceeds maximum allowed size of 20 MB");
        }
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new BusinessException("File must have a name");
        }

        // Retrieve customer
        var customer = customerLookup.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        try {
            String minioObjectName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(minioObjectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Build MinIO URL
            String minioUrl = "/api/v1/documents/download/" + minioObjectName;

            // Save metadata to database
            Document document = Document.builder()
                    .customerId(customer.id())
                    .documentType(documentType)
                    .fileName(file.getOriginalFilename())
                    .minioObjectName(minioObjectName)
                    .minioUrl(minioUrl)
                    .fileSize(file.getSize())
                    .build();

            Document savedDocument = documentRepository.save(document);

            // Publish upload event
            DocumentUploadedEvent event = new DocumentUploadedEvent(
                    savedDocument.getId(),
                    savedDocument.getCustomerId(),
                    savedDocument.getDocumentType(),
                    savedDocument.getMinioUrl()
            );
            eventPublisher.publishEvent(event);

            log.info("Document uploaded successfully with ID: {}", savedDocument.getId());
            return documentMapper.toDocumentResponse(savedDocument);

        } catch (Exception e) {
            log.error("Error uploading document", e);
            throw new RuntimeException("Error uploading document: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> getDocumentsByEmail(String email) {
        var customer = customerLookup.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        List<Document> documents = documentRepository.findByCustomerId(customer.id());
        return documents.stream()
                .map(documentMapper::toDocumentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DownloadResult downloadDocument(String email, UUID documentId) {
        log.info("Downloading document ID: {} for user: {}", documentId, email);

        var customer = customerLookup.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        Document document = documentRepository.findByIdAndCustomerId(documentId, customer.id())
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));

        try {
            var stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(document.getMinioObjectName())
                            .build()
            );

            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(document.getMinioObjectName())
                            .build()
            );

            return new DownloadResult(
                    document.getFileName(),
                    stat.contentType(),
                    stat.size(),
                    new InputStreamResource(inputStream)
            );
        } catch (Exception e) {
            log.error("Error downloading document", e);
            throw new RuntimeException("Error downloading document: " + e.getMessage());
        }
    }

    @Override
    public void deleteDocument(String email, UUID documentId) {
        log.info("Deleting document ID: {} for user: {}", documentId, email);

        // Retrieve customer
        var customer = customerLookup.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        Document document = documentRepository.findByIdAndCustomerId(documentId, customer.id())
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));

        try {
            // Delete from MinIO
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(document.getMinioObjectName())
                            .build()
            );

            // Delete from database
            documentRepository.delete(document);

            // Publish deletion event
            DocumentDeletedEvent event = new DocumentDeletedEvent(
                    document.getId(),
                    document.getCustomerId()
            );
            eventPublisher.publishEvent(event);

            log.info("Document deleted successfully with ID: {}", document.getId());

        } catch (Exception e) {
            log.error("Error deleting document", e);
            throw new RuntimeException("Error deleting document: " + e.getMessage());
        }
    }
}