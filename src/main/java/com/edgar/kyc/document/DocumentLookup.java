package com.edgar.kyc.document;

import java.util.UUID;

/**
 * Interface publique exposée par le module document pour les autres modules.
 */
public interface DocumentLookup {
    boolean existsByCustomerId(UUID customerId);
}
