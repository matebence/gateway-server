package com.blesk.gatewayserver.Component.WebSocket;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public interface WebSocketListener {

    void handleWebSocketDisconnectListener(SessionDisconnectEvent event);

    void injectSecurityContextViaAccessToken(String tokenValue);
}