package com.blesk.gatewayserver.Component.Feign;

import com.google.auth.oauth2.AccessToken;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientImpl implements FeignClient {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getDetails() instanceof AccessToken)) return;

        AccessToken accessToken = (AccessToken) authentication.getDetails();
        String tokenValue = accessToken.getTokenValue();
        requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, tokenValue));
    }
}