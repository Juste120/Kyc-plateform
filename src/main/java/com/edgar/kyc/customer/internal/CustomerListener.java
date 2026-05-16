package com.edgar.kyc.customer.internal;

import com.edgar.kyc.auth.AuthRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Listener pour les événements liés aux clients
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerListener {

    private final CustomerService customerService;

    /**
     * Écoute l'événement d'inscription pour créer automatiquement le profil client
     */
    @ApplicationModuleListener
    public void handleAuthRegisteredEvent(AuthRegisteredEvent event) {
        log.info("Handling AuthRegisteredEvent for user ID: {}", event.userId());

        customerService.createCustomer(
                event.email(),
                event.firstName(),
                event.lastName()
        );

        log.info("Customer profile created for user ID: {}", event.userId());
    }
}