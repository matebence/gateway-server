package com.blesk.gatewayserver.Controller;

import com.blesk.gatewayserver.DTO.Notifications;
import com.blesk.gatewayserver.Model.WebSocket;
import com.blesk.gatewayserver.Proxy.MessagingServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import com.blesk.gatewayserver.Service.NotificationsServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

import static java.lang.String.format;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class ConversationController {

    private SimpMessageSendingOperations simpMessageSendingOperations;

    private MessagingServiceProxy messagingServiceProxy;

    private NotificationsServiceImpl notificationsService;

    @Autowired
    public ConversationController(SimpMessageSendingOperations simpMessageSendingOperations, MessagingServiceProxy messagingServiceProxy, NotificationsServiceImpl notificationsService) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.messagingServiceProxy = messagingServiceProxy;
        this.notificationsService = notificationsService;
    }

    @MessageMapping("/state")
    public void setConversationState(@Payload @Valid WebSocket.Status status, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() == null) return;
        headerAccessor.getSessionAttributes().put("userName", status.getUserName());
        WebSocket.Status state = this.messagingServiceProxy.createStatus(status).getContent();
        if (state == null) return;
        this.simpMessageSendingOperations.convertAndSend("/status", state);
    }

    @MessageMapping("/conversations/{conversationId}/sendMessage")
    public void sendCommunicationMessage(@DestinationVariable String conversationId, @Payload @Valid WebSocket.Communications communications) {
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