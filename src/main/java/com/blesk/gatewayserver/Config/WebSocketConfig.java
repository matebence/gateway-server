package com.blesk.gatewayserver.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Value("${blesk.cors.allowed.origins}")
    private String origins;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        if(stompEndpointRegistry != null) stompEndpointRegistry.addEndpoint("/websocket").setAllowedOrigins(this.origins.split(", ")).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        if(messageBrokerRegistry != null){
            messageBrokerRegistry.setApplicationDestinationPrefixes("/websocket-service");
            messageBrokerRegistry.enableSimpleBroker("/communication", "/status", "conversation");
        }
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}