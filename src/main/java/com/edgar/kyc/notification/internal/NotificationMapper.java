package com.edgar.kyc.notification.internal;

import com.edgar.kyc.notification.internal.response.NotificationResponse;
import org.mapstruct.Mapper;

/**
 * Mapper pour convertir les entités Notification en DTOs
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toNotificationResponse(Notification notification);
}