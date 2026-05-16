package com.edgar.kyc.verification.internal;

import com.edgar.kyc.verification.internal.request.VerificationRejectRequest;
import com.edgar.kyc.verification.internal.response.VerificationResponse;

import java.util.List;
import java.util.UUID;

/**
 * Interface du service de gestion des vérifications KYC
 */
public interface VerificationService {
    VerificationResponse submitVerification(String email);
    List<VerificationResponse> getVerificationsByEmail(String email);
    List<VerificationResponse> getAllVerifications();
    VerificationResponse getVerificationById(UUID id);
    VerificationResponse reviewVerification(UUID id);
    VerificationResponse approveVerification(UUID id);
    VerificationResponse rejectVerification(UUID id, VerificationRejectRequest request);
}