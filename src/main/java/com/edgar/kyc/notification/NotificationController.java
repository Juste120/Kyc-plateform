package com.edgar.kyc.notification;

import com.edgar.kyc.notification.internal.NotificationService;
import com.edgar.kyc.notification.internal.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Contrôleur public pour la gestion des notifications
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Récupérer les notifications du client connecté
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Principal principal) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByEmail(principal.getName());
        return ResponseEntity.ok(notifications);
    }
}