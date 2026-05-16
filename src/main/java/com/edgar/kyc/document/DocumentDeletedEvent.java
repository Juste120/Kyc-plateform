package com.edgar.kyc.document;

import java.util.UUID;

/**
 * Event publié lorsqu'un document est supprimé
 * Contient toutes les informations nécessaires pour les autres modules
 */
public record DocumentDeletedEvent(
        UUID documentId,
        UUID customerId
) {
}