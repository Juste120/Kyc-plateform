package com.edgar.kyc.document.internal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse contenant les informations d'un document
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private UUID id;
    private UUID customerId;
    private String documentType;
    private String fileName;
    private String minioUrl;
    private Long fileSize;
    private LocalDateTime uploadedAt;
}