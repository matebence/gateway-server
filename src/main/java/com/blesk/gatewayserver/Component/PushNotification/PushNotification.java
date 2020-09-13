package com.blesk.gatewayserver.Component.PushNotification;

import com.blesk.gatewayserver.DTO.Notifications;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.WebpushConfig;

import java.util.concurrent.ExecutionException;

public interface PushNotification {

    Boolean sendNotification(Notifications notifications) throws InterruptedException, ExecutionException;

    String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException;

    WebpushConfig getWebpushConfig();

    Message getPreconfiguredMessageToToken(Notifications notifications);

    Builder getPreconfiguredMessageBuilder(Notifications notifications);
}