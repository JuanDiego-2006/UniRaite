package org.uniraite.uniraitebacked.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void inicializarFirebase() {
        try {
            String firebaseJson = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");

            InputStream serviceAccount;

            if (firebaseJson != null && !firebaseJson.isEmpty()) {
                // En Railway: lee desde la variable de entorno
                serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));
                System.out.println("🔥 Usando Firebase desde variable de entorno");
            } else {
                // En local: lee desde el archivo JSON
                serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream("firebase-service-account.json");
                System.out.println("🔥 Usando Firebase desde archivo local");
            }

            if (serviceAccount != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    System.out.println("✅ Firebase Admin SDK inicializado correctamente");
                } else {
                    System.out.println("⚠️ Firebase ya estaba inicializado");
                }
            } else {
                System.out.println("❌ No se encontró configuración de Firebase");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al inicializar Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}