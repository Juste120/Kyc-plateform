package com.edgar.kyc.verification;

import com.edgar.kyc.verification.internal.VerificationService;
import com.edgar.kyc.verification.internal.request.VerificationRejectRequest;
import com.edgar.kyc.verification.internal.response.VerificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur public pour la gestion des vérifications KYC
 */
@RestController
@RequestMapping("/api/v1/verifications")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    /**
     * Soumettre un dossier KYC
     */
    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VerificationResponse> submitVerification(Principal principal) {
        VerificationResponse verification = verificationService.submitVerification(principal.getName());
        return ResponseEntity.ok(verification);
    }

    /**
     * Récupérer les vérifications du client connecté
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<VerificationResponse>> getMyVerifications(Principal principal) {
        List<VerificationResponse> verifications = verificationService.getVerificationsByEmail(principal.getName());
        return ResponseEntity.ok(verifications);
    }

    /**
     * Récupérer toutes les vérifications (pour admin)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VerificationResponse>> getAllVerifications() {
        List<VerificationResponse> verifications = verificationService.getAllVerifications();
        return ResponseEntity.ok(verifications);
    }

    /**
     * Récupérer une vérification par ID (pour admin)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VerificationResponse> getVerification(@PathVariable UUID id) {
        VerificationResponse verification = verificationService.getVerificationById(id);
        return ResponseEntity.ok(verification);
    }

    /**
     * Passer une vérification en revue
     */
    @PatchMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VerificationResponse> reviewVerification(@PathVariable UUID id) {
        VerificationResponse verification = verificationService.reviewVerification(id);
        return ResponseEntity.ok(verification);
    }

    /**
     * Approuver une vérification
     */
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VerificationResponse> approveVerification(@PathVariable UUID id) {
        VerificationResponse verification = verificationService.approveVerification(id);
        return ResponseEntity.ok(verification);
    }

    /**
     * Rejeter une vérification
     */
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VerificationResponse> rejectVerification(
            @PathVariable UUID id,
            @Valid @RequestBody VerificationRejectRequest request) {
        VerificationResponse verification = verificationService.rejectVerification(id, request);
        return ResponseEntity.ok(verification);
    }
}