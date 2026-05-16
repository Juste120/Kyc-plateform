package com.edgar.kyc.auth;

import java.util.UUID;

/**
 * Event publié lorsqu'un utilisateur s'inscrit avec succès
 * Contient toutes les informations nécessaires pour créer le profil client
 */
public record AuthRegisteredEvent(
        UUID userId,
        String email,
        String firstName,
        String lastName
) {
}