package com.edgar.kyc.verification.internal.response;

import com.edgar.kyc.verification.internal.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse contenant les informations d'une vérification
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationResponse {

    private UUID id;
    private UUID customerId;
    private String customerEmail;
    private String customerFirstName;
    private VerificationStatus status;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
}