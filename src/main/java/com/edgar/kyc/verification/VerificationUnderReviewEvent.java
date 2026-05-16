package com.edgar.kyc.verification;

import java.util.UUID;

/**
 * Event publié lorsqu'un dossier KYC est mis en revue
 * Contient toutes les informations nécessaires pour les autres modules
 */
public record VerificationUnderReviewEvent(
        UUID verificationId,
        UUID customerId,
        String customerEmail,
        String customerFirstName
) {
}