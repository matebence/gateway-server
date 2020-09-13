package com.blesk.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@EnableZuulProxy
@EnableWebSecurity
@EnableFeignClients
@EnableResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableWebSocketMessageBroker
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}
}