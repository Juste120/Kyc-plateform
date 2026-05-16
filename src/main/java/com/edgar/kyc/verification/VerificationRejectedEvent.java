package com.edgar.kyc.verification;

import java.util.UUID;

/**
 * Event publié lorsqu'un dossier KYC est rejeté
 * Contient toutes les informations nécessaires pour les autres modules
 */
public record VerificationRejectedEvent(
        UUID verificationId,
        UUID customerId,
        String customerEmail,
        String customerFirstName,
        String rejectionReason
) {
}