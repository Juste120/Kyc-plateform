package com.edgar.kyc.notification.internal;

import com.edgar.kyc.verification.VerificationApprovedEvent;
import com.edgar.kyc.verification.VerificationRejectedEvent;
import com.edgar.kyc.verification.VerificationSubmittedEvent;
import com.edgar.kyc.verification.VerificationUnderReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Listener pour les événements liés aux notifications
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    /**
     * Écoute l'événement de soumission de vérification
     */
    @ApplicationModuleListener
    public void handleVerificationSubmittedEvent(VerificationSubmittedEvent event) {
        log.info("Handling VerificationSubmittedEvent for customer: {}", event.customerEmail());

        String subject = "KYC Dossier Received";
        String message = String.format(
                "Hello %s,\n\nWe have received your KYC submission. Our team will review your documents shortly.\n\nThank you for your patience.",
                event.customerFirstName()
        );

        notificationService.sendNotification(event.customerEmail(), subject, message);
    }

    /**
     * Écoute l'événement de mise en revue de vérification
     */
    @ApplicationModuleListener
    public void handleVerificationUnderReviewEvent(VerificationUnderReviewEvent event) {
        log.info("Handling VerificationUnderReviewEvent for customer: {}", event.customerEmail());

        String subject = "KYC Dossier Under Review";
        String message = String.format(
                "Hello %s,\n\nYour KYC dossier is currently under review by our compliance team.\n\nWe will notify you of the outcome soon.",
                event.customerFirstName()
        );

        notificationService.sendNotification(event.customerEmail(), subject, message);
    }

    /**
     * Écoute l'événement d'approbation de vérification
     */
    @ApplicationModuleListener
    public void handleVerificationApprovedEvent(VerificationApprovedEvent event) {
        log.info("Handling VerificationApprovedEvent for customer: {}", event.customerEmail());

        String subject = "KYC Dossier Approved";
        String message = String.format(
                "Hello %s,\n\nCongratulations! Your KYC dossier has been approved.\n\nYou can now enjoy all features of our platform.",
                event.customerFirstName()
        );

        notificationService.sendNotification(event.customerEmail(), subject, message);
    }

    /**
     * Écoute l'événement de rejet de vérification
     */
    @ApplicationModuleListener
    public void handleVerificationRejectedEvent(VerificationRejectedEvent event) {
        log.info("Handling VerificationRejectedEvent for customer: {}", event.customerEmail());

        String subject = "KYC Dossier Rejected";
        String message = String.format(
                "Hello %s,\n\nWe regret to inform you that your KYC dossier has been rejected.\n\nReason: %s\n\nPlease review your documents and resubmit if necessary.",
                event.customerFirstName(),
                event.rejectionReason()
        );

        notificationService.sendNotification(event.customerEmail(), subject, message);
    }
}