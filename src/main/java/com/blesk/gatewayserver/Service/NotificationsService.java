package com.blesk.gatewayserver.Service;

import com.blesk.gatewayserver.DTO.Notifications;

public interface NotificationsService {

    Boolean sendPushNotificationToToken(Notifications notifications);
}