package com.blesk.gatewayserver.Tool;

import com.google.auth.oauth2.AccessToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.HashMap;
import java.util.HashSet;

public class SecurityContextManger {

    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    private OAuth2Authentication oAuth2Authentication;

    private OAuth2Request oAuth2Request;

    private AccessToken accessToken;

    private User user;

    public void buildSecurityContext(String accessToken, String userName) {
        this.setoAuth2Request(new OAuth2Request(new HashMap<String, String>(), "client_id", new HashSet<GrantedAuthority>(), true, new HashSet<String>(), null, "", new HashSet<String>(), null));
        this.setUser(new User(userName, "", true, true, true, true, new HashSet<GrantedAuthority>()));
        this.setUsernamePasswordAuthenticationToken(new UsernamePasswordAuthenticationToken(getUser(), null, new HashSet<GrantedAuthority>()));
        this.setoAuth2Authentication(new OAuth2Authentication(getoAuth2Request(), getUsernamePasswordAuthenticationToken()));
        this.setAccessToken(new AccessToken(accessToken, null));
        getoAuth2Authentication().setDetails(getAccessToken());
        getoAuth2Authentication().setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(getoAuth2Authentication());
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken() {
        return usernamePasswordAuthenticationToken;
    }

    private void setUsernamePasswordAuthenticationToken(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
        this.usernamePasswordAuthenticationToken = usernamePasswordAuthenticationToken;
    }

    private OAuth2Authentication getoAuth2Authentication() {
        return oAuth2Authentication;
    }

    private void setoAuth2Authentication(OAuth2Authentication oAuth2Authentication) {
        this.oAuth2Authentication = oAuth2Authentication;
    }

    private OAuth2Request getoAuth2Request() {
        return oAuth2Request;
    }

    private void setoAuth2Request(OAuth2Request oAuth2Request) {
        this.oAuth2Request = oAuth2Request;
    }

    private AccessToken getAccessToken() {
        return accessToken;
    }

    private void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    private User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }
}