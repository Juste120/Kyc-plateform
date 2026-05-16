package com.edgar.kyc.document;

import java.util.UUID;

/**
 * Event publié lorsqu'un document est uploadé avec succès
 * Contient toutes les informations nécessaires pour les autres modules
 */
public record DocumentUploadedEvent(
        UUID documentId,
        UUID customerId,
        String documentType,
        String minioUrl
) {
}