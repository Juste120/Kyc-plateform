package com.edgar.kyc.notification.internal;

import com.edgar.kyc.notification.internal.response.NotificationResponse;

import java.util.List;

/**
 * Interface du service de gestion des notifications
 */
public interface NotificationService {
    List<NotificationResponse> getNotificationsByEmail(String email);
    void sendNotification(String email, String subject, String message);
}