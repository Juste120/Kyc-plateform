package com.edgar.kyc.notification.internal;

import com.edgar.kyc.customer.CustomerLookup;
import com.edgar.kyc.exception.ResourceNotFoundException;
import com.edgar.kyc.notification.internal.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation du service de gestion des notifications
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomerLookup customerLookup;
    private final NotificationMapper notificationMapper;
    private final JavaMailSender mailSender;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByEmail(String email) {
        List<Notification> notifications = notificationRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    @Override
    public void sendNotification(String email, String subject, String message) {
        log.info("Sending notification to email: {}", email);

        try {
            // Envoyer l'email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);

            // Sauvegarder la notification en base
            var customer = customerLookup.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

            Notification notification = Notification.builder()
                    .customerId(customer.id())
                    .customerEmail(email)
                    .subject(subject)
                    .message(message)
                    .build();

            notificationRepository.save(notification);

            log.info("Notification sent successfully to email: {}", email);

        } catch (Exception e) {
            log.error("Error sending notification to email: {}", email, e);
            throw new RuntimeException("Error sending notification: " + e.getMessage());
        }
    }
}