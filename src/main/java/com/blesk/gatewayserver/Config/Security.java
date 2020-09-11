package com.blesk.gatewayserver.Config;

import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
public class Security extends WebSecurityConfigurerAdapter {

    @Value("${blesk.cors.allowed.origins}")
    private String origins;

    @Value("${blesk.cors.allowed.methods}")
    private String methods;

    @Value("${blesk.cors.allowed.headers}")
    private String headers;

    @Value("${blesk.cors.allowed.exposed}")
    private String exposed;

    @Value("${blesk.cors.allowed.age}")
    private Long age;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.stream(Http.HttpMethod.values()).map(Http.HttpMethod::name).collect(Collectors.toList()));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        CorsConfiguration config = new CorsConfiguration();
//        config.setMaxAge(this.age);
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Arrays.asList(this.origins.split(", ")));
//        config.setAllowedHeaders(Arrays.asList(this.headers.split(", ")));
//        config.setAllowedMethods(Arrays.asList(this.methods.split(", ")));
//        config.setExposedHeaders(Arrays.asList(this.exposed.split(", ")));
//
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
//        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//
//        return filterRegistrationBean;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}