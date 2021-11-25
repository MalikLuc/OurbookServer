package com.example.v2ourbook.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;


@Configuration
public class FirebaseConfig {

//    private static final String GOOGLE_APPLICATION_CREDENTIALS = "/home/yannx/IdeaProjects/v2ourbook/src/main/resources/firebase_config.json";

    @Bean
    public FirebaseAuth firebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    @PostConstruct
    public void initializeFirebaseApp() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("src/main/resources/firebase_config.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ourbook-357db-default-rtdb.europe-west1.firebasedatabase.app")
//                .setServiceAccountId(GOOGLE_APPLICATION_CREDENTIALS)
                .setStorageBucket("ourbook-357db.appspot.com")
                .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
