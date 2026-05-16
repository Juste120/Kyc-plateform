package com.edgar.kyc.customer;

import java.util.UUID;

/**
 * Event publié lorsque le profil d'un client est mis à jour
 * Contient toutes les informations nécessaires pour les autres modules
 */
public record CustomerUpdatedEvent(
        UUID customerId,
        String email,
        String firstName,
        String lastName,
        String phone
) {
}