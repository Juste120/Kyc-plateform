package com.edgar.kyc.verification;

import java.util.UUID;

/**
 * Event publié lorsqu'un dossier KYC est approuvé
 * Contient toutes les informations nécessaires pour les autres modules
 */
public record VerificationApprovedEvent(
        UUID verificationId,
        UUID customerId,
        String customerEmail,
        String customerFirstName
) {
}