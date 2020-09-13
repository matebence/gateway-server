package com.blesk.gatewayserver.Component.WebSocket;

import com.blesk.gatewayserver.Model.WebSocket;
import com.blesk.gatewayserver.Proxy.MessagingServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketListenerImpl implements WebSocketListener {

    private SimpMessageSendingOperations simpMessageSendingOperations;
    private MessagingServiceProxy messagingServiceProxy;

    @Autowired
    public WebSocketListenerImpl(SimpMessageSendingOperations simpMessageSendingOperations, MessagingServiceProxy messagingServiceProxy) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.messagingServiceProxy = messagingServiceProxy;
    }

    @Override
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getSessionAttributes() == null) return;
        String userName = (String) headerAccessor.getSessionAttributes().get("userName");

        if (userName == null) return;
        WebSocket.Status status = new WebSocket.Status();
        status.setState(WebSocket.State.OFFLINE.name());
        status.setToken(null);
        status.setUserName(userName);
        WebSocket.Status state = this.messagingServiceProxy.createStatus(status).getContent();

        if (state == null) return;
        this.simpMessageSendingOperations.convertAndSend("/status", state);
    }
}