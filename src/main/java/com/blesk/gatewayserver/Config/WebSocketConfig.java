package com.blesk.gatewayserver.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        if(stompEndpointRegistry != null) stompEndpointRegistry.addEndpoint("/websocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        if(messageBrokerRegistry != null){
            messageBrokerRegistry.setApplicationDestinationPrefixes("/websocket-service");
            messageBrokerRegistry.enableSimpleBroker("/conversations", "/status");
        }
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}