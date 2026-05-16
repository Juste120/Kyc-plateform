package com.edgar.kyc.document.internal;

import com.edgar.kyc.document.DocumentLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DocumentLookupImpl implements DocumentLookup {

    private final DocumentRepository documentRepository;

    @Override
    public boolean existsByCustomerId(UUID customerId) {
        return documentRepository.existsByCustomerId(customerId);
    }
}
