package com.edgar.kyc.document.internal;

import com.edgar.kyc.document.internal.response.DocumentResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Interface du service de gestion des documents
 */
public interface DocumentService {
    DocumentResponse uploadDocument(String email, MultipartFile file, String documentType);
    List<DocumentResponse> getDocumentsByEmail(String email);
    DownloadResult downloadDocument(String email, UUID documentId);
    void deleteDocument(String email, UUID documentId);

    record DownloadResult(String originalFileName, String contentType, long contentLength, InputStreamResource inputStream) {
    }
}