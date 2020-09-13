package com.blesk.gatewayserver.Config;

import com.blesk.gatewayserver.Exception.GatewayServerException;
import com.blesk.gatewayserver.Value.Messages;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class Firebase {

    @Value("${blesk.firebase.configuration-file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())).build();
            if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new GatewayServerException(Messages.FIREBASE_EXCEPTION, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}