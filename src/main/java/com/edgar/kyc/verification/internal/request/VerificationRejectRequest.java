package com.edgar.kyc.verification.internal.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO pour la requête de rejet d'une vérification
 */
@Data
public class VerificationRejectRequest {

    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;
}