package com.blesk.gatewayserver.Component.WebSocket;

import com.blesk.gatewayserver.Model.WebSocket;
import com.blesk.gatewayserver.Proxy.MessagingServiceProxy;
import com.google.auth.oauth2.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.HashSet;

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
        String accessToken = (String) headerAccessor.getSessionAttributes().get("accessToken");
        if (userName == null || accessToken == null) return;

        this.handleTempSecurityContext(accessToken);

        WebSocket.Status status = new WebSocket.Status();
        status.setState(WebSocket.State.OFFLINE.name());
        status.setToken(null);
        status.setUserName(userName);
        WebSocket.Status state = this.messagingServiceProxy.createStatus(status).getContent();

        if (state == null) return;
        this.simpMessageSendingOperations.convertAndSend("/status", state);
    }

    @Override
    public void handleTempSecurityContext(String accessToken) {
        OAuth2Request oAuth2Request = new OAuth2Request(new HashMap<String, String>(), "client_id", new HashSet<GrantedAuthority>(), true, new HashSet<String>(), null, "", new HashSet<String>(), null);
        User userPrincipal = new User("user", "", true, true, true, true, new HashSet<GrantedAuthority>());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, new HashSet<GrantedAuthority>());

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);
        AccessToken tokenValue = new AccessToken(accessToken, null);
        oAuth2Authentication.setDetails(tokenValue);
        oAuth2Authentication.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
    }
}