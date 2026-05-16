package com.edgar.kyc.verification.internal;

import com.edgar.kyc.customer.CustomerLookup;
import com.edgar.kyc.document.DocumentLookup;
import com.edgar.kyc.exception.BusinessException;
import com.edgar.kyc.exception.ResourceNotFoundException;
import com.edgar.kyc.verification.VerificationApprovedEvent;
import com.edgar.kyc.verification.VerificationRejectedEvent;
import com.edgar.kyc.verification.VerificationSubmittedEvent;
import com.edgar.kyc.verification.VerificationUnderReviewEvent;
import com.edgar.kyc.verification.internal.request.VerificationRejectRequest;
import com.edgar.kyc.verification.internal.response.VerificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service de gestion des vérifications KYC
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final CustomerLookup customerLookup;
    private final DocumentLookup documentLookup;
    private final VerificationMapper verificationMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public VerificationResponse submitVerification(String email) {
        log.info("Submitting verification for user: {}", email);

        // Récupérer le client
        var customer = customerLookup.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        // Vérifier s'il y a des documents
        if (!documentLookup.existsByCustomerId(customer.id())) {
            throw new BusinessException("Please upload at least one document before submitting for verification.");
        }

        // Créer la vérification
        Verification verification = Verification.builder()
                .customerId(customer.id())
                .customerEmail(customer.email())
                .customerFirstName(customer.firstName())
                .status(VerificationStatus.PENDING)
                .build();

        Verification savedVerification = verificationRepository.save(verification);

        // Publier l'événement de soumission
        VerificationSubmittedEvent event = new VerificationSubmittedEvent(
                savedVerification.getId(),
                savedVerification.getCustomerId(),
                savedVerification.getCustomerEmail(),
                savedVerification.getCustomerFirstName()
        );
        eventPublisher.publishEvent(event);

        log.info("Verification submitted successfully with ID: {}", savedVerification.getId());
        return verificationMapper.toVerificationResponse(savedVerification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificationResponse> getVerificationsByEmail(String email) {
        var customer = customerLookup.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        List<Verification> verifications = verificationRepository.findByCustomerId(customer.id());
        return verifications.stream()
                .map(verificationMapper::toVerificationResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificationResponse> getAllVerifications() {
        List<Verification> verifications = verificationRepository.findAllByOrderBySubmittedAtDesc();
        return verifications.stream()
                .map(verificationMapper::toVerificationResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationResponse getVerificationById(UUID id) {
        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Verification", "id", id));
        return verificationMapper.toVerificationResponse(verification);
    }

    @Override
    public VerificationResponse reviewVerification(UUID id) {
        log.info("Reviewing verification ID: {}", id);

        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Verification", "id", id));

        verification.setStatus(VerificationStatus.UNDER_REVIEW);
        Verification updatedVerification = verificationRepository.save(verification);

        // Publier l'événement de mise en revue
        VerificationUnderReviewEvent event = new VerificationUnderReviewEvent(
                updatedVerification.getId(),
                updatedVerification.getCustomerId(),
                updatedVerification.getCustomerEmail(),
                updatedVerification.getCustomerFirstName()
        );
        eventPublisher.publishEvent(event);

        log.info("Verification reviewed successfully with ID: {}", updatedVerification.getId());
        return verificationMapper.toVerificationResponse(updatedVerification);
    }

    @Override
    public VerificationResponse approveVerification(UUID id) {
        log.info("Approving verification ID: {}", id);

        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Verification", "id", id));

        verification.setStatus(VerificationStatus.APPROVED);
        Verification updatedVerification = verificationRepository.save(verification);

        // Publier l'événement d'approbation
        VerificationApprovedEvent event = new VerificationApprovedEvent(
                updatedVerification.getId(),
                updatedVerification.getCustomerId(),
                updatedVerification.getCustomerEmail(),
                updatedVerification.getCustomerFirstName()
        );
        eventPublisher.publishEvent(event);

        log.info("Verification approved successfully with ID: {}", updatedVerification.getId());
        return verificationMapper.toVerificationResponse(updatedVerification);
    }

    @Override
    public VerificationResponse rejectVerification(UUID id, VerificationRejectRequest request) {
        log.info("Rejecting verification ID: {}", id);

        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Verification", "id", id));

        verification.setStatus(VerificationStatus.REJECTED);
        verification.setRejectionReason(request.getRejectionReason());
        Verification updatedVerification = verificationRepository.save(verification);

        // Publier l'événement de rejet
        VerificationRejectedEvent event = new VerificationRejectedEvent(
                updatedVerification.getId(),
                updatedVerification.getCustomerId(),
                updatedVerification.getCustomerEmail(),
                updatedVerification.getCustomerFirstName(),
                updatedVerification.getRejectionReason()
        );
        eventPublisher.publishEvent(event);

        log.info("Verification rejected successfully with ID: {}", updatedVerification.getId());
        return verificationMapper.toVerificationResponse(updatedVerification);
    }
}