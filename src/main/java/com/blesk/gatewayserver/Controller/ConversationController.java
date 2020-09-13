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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
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
        WebSocket.Status state = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (headerAccessor.getSessionAttributes() == null || authentication == null) return;
        if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            headerAccessor.getSessionAttributes().put("userName", status.getUserName());
            headerAccessor.getSessionAttributes().put("accessToken", details.getTokenValue());
            state = this.messagingServiceProxy.createStatus(status).getContent();
        }
        if (state == null) return;
        this.simpMessageSendingOperations.convertAndSend("/status", state);
    }

    @MessageMapping("/conversations/{conversationId}/sendMessage")
    public void sendCommunicationMessage(@DestinationVariable String conversationId, @Payload @Valid WebSocket.Communications communications) {
        WebSocket.Communications communication = this.messagingServiceProxy.createCommunications(communications).getContent();
        if (communication == null) return;
        for (WebSocket.Users users : communication.getConversations().getParticipants()) {
            if (!communication.getSender().equals(users.getAccountId())) {
                WebSocket.Status status = this.messagingServiceProxy.retrieveStatus(users.getStatus().getStatusId()).getContent();

                Notifications notifications = new Notifications();
                notifications.setBody(communication.getContent());
                notifications.setToken(status.getToken());
                notifications.setData(new HashMap<String, String>(){{put("lastConversionId", communication.getCommunicationId());}});

                if (communication.getContent().length() > 5) notifications.setBody(communication.getContent().substring(0, 5).concat("..."));
                notifications.setTitle(communication.getConversations().getParticipants().stream().filter(user -> !communication.getSender().equals(user.getAccountId())).map(userName -> userName.getUserName().concat(" ")).reduce("", String::concat));

                this.notificationsService.sendPushNotificationToToken(notifications);
            }
        }
        this.simpMessageSendingOperations.convertAndSend(format("/conversations/%s", conversationId), communication);
    }
}