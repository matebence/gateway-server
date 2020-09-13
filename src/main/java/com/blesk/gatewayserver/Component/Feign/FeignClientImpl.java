package com.blesk.gatewayserver.Component.Feign;

import com.google.auth.oauth2.AccessToken;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class FeignClientImpl implements FeignClient {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return;

        String tokenValue;
        if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
            tokenValue = auth2AuthenticationDetails.getTokenValue();
        } else if (authentication.getDetails() instanceof AccessToken) {
            AccessToken accessToken = (AccessToken) authentication.getDetails();
            tokenValue = accessToken.getTokenValue();
        } else {
            return;
        }
        requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, tokenValue));
    }
}