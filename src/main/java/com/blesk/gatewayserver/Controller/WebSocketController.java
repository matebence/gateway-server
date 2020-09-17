package com.blesk.gatewayserver.Controller;

import com.blesk.gatewayserver.DTO.Notifications;
import com.blesk.gatewayserver.Model.WebSocket;
import com.blesk.gatewayserver.Proxy.MessagingServiceProxy;
import com.blesk.gatewayserver.Tool.SecurityContextManger;
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
import java.util.Map;

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
    public void setConversationState(@Payload Map payload, SimpMessageHeaderAccessor headerAccessor) {
        WebSocket.Status status = (WebSocket.Status) payload.get("status");
        WebSocket.AccessToken accessToken = (WebSocket.AccessToken) payload.get("accessToken");

        if (headerAccessor.getSessionAttributes() == null || status == null || accessToken == null) return;
        headerAccessor.getSessionAttributes().put("userName", status.getUserName());
        headerAccessor.getSessionAttributes().put("accessToken", accessToken.getToken());

        SecurityContextManger securityContextManger = new SecurityContextManger();
        securityContextManger.buildSecurityContext(accessToken.getToken(), status.getUserName());
        WebSocket.Status state = this.messagingServiceProxy.createStatus(status).getContent();

        if (state == null) return;
        this.simpMessageSendingOperations.convertAndSend("/status", state);
    }

    @MessageMapping("/conversations/{conversationId}/sendMessage")
    public void sendCommunicationMessage(@DestinationVariable String conversationId, @Payload Map payload) {
        WebSocket.Communications communications = (WebSocket.Communications) payload.get("communications");
        WebSocket.AccessToken accessToken = (WebSocket.AccessToken) payload.get("accessToken");
        if (communications == null || accessToken == null) return;

        SecurityContextManger securityContextManger = new SecurityContextManger();
        securityContextManger.buildSecurityContext(accessToken.getToken(), communications.getUserName());

        WebSocket.Communications communication = this.messagingServiceProxy.createCommunications(communications).getContent();
        if (communication == null) return;

        for (WebSocket.Users users : communications.getConversations().getParticipants()) {
            if (!communications.getSender().equals(users.getAccountId())) {
                WebSocket.Status status = this.messagingServiceProxy.retrieveStatus(users.getStatus().getStatusId()).getContent();

                Notifications notifications = new Notifications();
                notifications.setBody(communications.getContent());
                notifications.setToken(status.getToken());
                notifications.setData(new HashMap<String, String>(){{put("lastConversionId", communication.getCommunicationId());}});

                if (communications.getContent().length() > 5) notifications.setBody(communications.getContent().substring(0, 5).concat("..."));
                notifications.setTitle(communications.getConversations().getParticipants().stream().filter(user -> !communications.getSender().equals(user.getAccountId())).map(userName -> userName.getUserName().concat(" ")).reduce("", String::concat));

                this.notificationsService.sendPushNotificationToToken(notifications);
            }
        }
        this.simpMessageSendingOperations.convertAndSend(format("/conversations/%s", conversationId), communications);
    }
}