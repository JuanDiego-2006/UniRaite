package org.uniraite.uniraitebacked.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void inicializarFirebase() {
        try {
            // Lee el archivo JSON que guardaste en resources
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");

            if (serviceAccount != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    System.out.println("🔥 Firebase Admin SDK inicializado correctamente 🔥");
                }
            } else {
                System.out.println("❌ No se encontró el archivo firebase-service-account.json");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}