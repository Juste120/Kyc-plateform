package com.edgar.kyc.verification.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Verification
 */
@Repository
public interface VerificationRepository extends JpaRepository<Verification, UUID> {
    List<Verification> findByCustomerId(UUID customerId);
    List<Verification> findAllByOrderBySubmittedAtDesc();
    Optional<Verification> findByIdAndCustomerId(UUID id, UUID customerId);
}