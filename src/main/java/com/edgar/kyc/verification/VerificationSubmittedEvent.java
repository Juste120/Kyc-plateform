package com.edgar.kyc.verification;

import java.util.UUID;

/**
 * Event publié lorsqu'un dossier KYC est soumis
 * Contient toutes les informations nécessaires pour les autres modules
 */
public record VerificationSubmittedEvent(
        UUID verificationId,
        UUID customerId,
        String customerEmail,
        String customerFirstName
) {
}