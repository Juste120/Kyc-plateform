package com.edgar.kyc.customer;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface publique exposée par le module customer pour les autres modules.
 */
public interface CustomerLookup {
    Optional<CustomerInfo> findByEmail(String email);

    record CustomerInfo(UUID id, String email, String firstName) {
    }
}
