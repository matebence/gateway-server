package com.blesk.gatewayserver.Component.PushNotification;

import com.blesk.gatewayserver.DTO.Notifications;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class PushNotificationImpl implements PushNotification {

    @Override
    public Boolean sendNotification(Notifications notifications) throws InterruptedException, ExecutionException {
        return sendAndGetResponse(getPreconfiguredMessageToToken(notifications)) != null;
    }

    @Override
    public String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    @Override
    public WebpushConfig getWebpushConfig(){
        return WebpushConfig.builder().putHeader("ttl", "300").setNotification(WebpushNotification.builder().build()).build();
    }

    @Override
    public Message getPreconfiguredMessageToToken(Notifications notifications) {
        return getPreconfiguredMessageBuilder(notifications).setToken(notifications.getToken()).build();
    }

    @Override
    public Message.Builder getPreconfiguredMessageBuilder(Notifications notifications) {
        return  Message.builder().putAllData(notifications.getData()).setWebpushConfig(getWebpushConfig()).setNotification(Notification.builder().setBody(notifications.getBody()).setTitle(notifications.getTitle()).setImage(notifications.getImageUrl()).build());
    }
}