package com.blesk.gatewayserver.Controller;

import com.blesk.gatewayserver.DTO.Notifications;
import com.blesk.gatewayserver.DTO.Websocket;
import com.blesk.gatewayserver.Model.Model;
import com.blesk.gatewayserver.Proxy.MessagingServiceProxy;
import com.blesk.gatewayserver.Config.SecurityContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import com.blesk.gatewayserver.Service.NotificationsServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static java.lang.String.format;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class WebSocketController {

    private SimpMessageSendingOperations simpMessageSendingOperations;

    private MessagingServiceProxy messagingServiceProxy;

    private NotificationsServiceImpl notificationsService;

    @Autowired
    public WebSocketController(SimpMessageSendingOperations simpMessageSendingOperations, MessagingServiceProxy messagingServiceProxy, NotificationsServiceImpl notificationsService) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.messagingServiceProxy = messagingServiceProxy;
        this.notificationsService = notificationsService;
    }

    @MessageMapping("/state")
    public void setConversationState(@Payload Websocket.Status websocket, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() == null || websocket == null || websocket.getStatus() == null || websocket.getStatus() == null) return;
        headerAccessor.getSessionAttributes().put("accessToken", websocket.getAccessToken().getToken());
        headerAccessor.getSessionAttributes().put("userName", websocket.getStatus().getUserName());

        SecurityContextManager securityContextManager = new SecurityContextManager();
        securityContextManager.buildSecurityContext(websocket.getAccessToken().getToken(), websocket.getStatus().getUserName());
        Model.Status state = this.messagingServiceProxy.createStatus(websocket.getStatus()).getContent();

        if (state == null) return;
        this.simpMessageSendingOperations.convertAndSend("/status", state);
    }

    @MessageMapping("/conversations/{conversationId}/sendMessage")
    public void sendCommunicationMessage(@DestinationVariable String conversationId, @Payload Websocket.Communications websocket) {
        if (websocket == null || websocket.getCommunications() == null || websocket.getAccessToken() == null) return;

        SecurityContextManager securityContextManager = new SecurityContextManager();
        securityContextManager.buildSecurityContext(websocket.getAccessToken().getToken(), websocket.getCommunications().getUserName());

        Model.Communications communication = this.messagingServiceProxy.createCommunications(websocket.getCommunications()).getContent();
        if (communication == null) return;

        for (Model.Users users : websocket.getCommunications().getConversations().getParticipants()) {
            if (!websocket.getCommunications().getSender().equals(users.getAccountId())) {
                Model.Status status = this.messagingServiceProxy.retrieveStatus(users.getStatus().getStatusId()).getContent();

                Notifications notifications = new Notifications();
                notifications.setBody(websocket.getCommunications().getContent());
                notifications.setToken(status.getToken());
                notifications.setData(new HashMap<String, String>(){{put("lastConversationId", communication.getCommunicationId());}});

                if (websocket.getCommunications().getContent().length() > 5) notifications.setBody(websocket.getCommunications().getContent().substring(0, 5).concat("..."));
                notifications.setTitle(websocket.getCommunications().getConversations().getParticipants().stream().filter(user -> !websocket.getCommunications().getSender().equals(user.getAccountId())).map(userName -> userName.getStatus().getUserName().concat(" ")).reduce("", String::concat));

                this.notificationsService.sendPushNotificationToToken(notifications);
            }
        }
        this.simpMessageSendingOperations.convertAndSend(format("/conversations/%s", conversationId), websocket.getCommunications());
    }
}