package com.edgar.kyc.document.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Document
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByCustomerId(UUID customerId);
    Optional<Document> findByIdAndCustomerId(UUID id, UUID customerId);
    boolean existsByCustomerId(UUID customerId);
}