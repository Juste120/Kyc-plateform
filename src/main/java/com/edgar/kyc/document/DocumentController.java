package com.edgar.kyc.document;

import com.edgar.kyc.document.internal.DocumentService;
import com.edgar.kyc.document.internal.response.DocumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur public pour la gestion des documents
 */
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Upload d'un document
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DocumentResponse> uploadDocument(
            Principal principal,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType) {
        DocumentResponse document = documentService.uploadDocument(principal.getName(), file, documentType);
        return ResponseEntity.ok(document);
    }

    /**
     * Récupérer tous les documents du client connecté
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<DocumentResponse>> getMyDocuments(Principal principal) {
        List<DocumentResponse> documents = documentService.getDocumentsByEmail(principal.getName());
        return ResponseEntity.ok(documents);
    }

    /**
     * Télécharger un document
     */
    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InputStreamResource> downloadDocument(Principal principal, @PathVariable UUID id) {
        DocumentService.DownloadResult result = documentService.downloadDocument(principal.getName(), id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.contentType()))
                .contentLength(result.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.originalFileName() + "\"")
                .body(result.inputStream());
    }

    /**
     * Supprimer un document
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteDocument(Principal principal, @PathVariable UUID id) {
        documentService.deleteDocument(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}