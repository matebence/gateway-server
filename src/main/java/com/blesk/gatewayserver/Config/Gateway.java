package com.blesk.gatewayserver.Config;

import com.blesk.gatewayserver.EntryPoint.OAuthEntryPoint;
import com.blesk.gatewayserver.Handler.OAuthHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class Gateway extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/authorization-server/**").permitAll().antMatchers("/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(new OAuthEntryPoint()).accessDeniedHandler(new OAuthHandler());
    }
}