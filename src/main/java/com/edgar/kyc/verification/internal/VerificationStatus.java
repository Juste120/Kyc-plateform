package com.edgar.kyc.verification.internal;

/**
 * Statuts possibles pour une vérification KYC
 */
public enum VerificationStatus {
    PENDING,        // Soumis mais pas encore examiné
    UNDER_REVIEW,   // En cours d'examen
    APPROVED,       // Approuvé
    REJECTED        // Rejeté
}