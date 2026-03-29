package org.uniraite.uniraitebacked.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    public void enviarNotificacionPush(String tokenDestino, String titulo, String cuerpo) {
        if (tokenDestino == null || tokenDestino.isEmpty()) {
            return; // Si el usuario no tiene token, no hacemos nada
        }

        try {
            // Construimos la notificación para la nube de Google
            Notification notification = Notification.builder()
                    .setTitle(titulo)
                    .setBody(cuerpo)
                    .build();

            Message message = Message.builder()
                    .setToken(tokenDestino)
                    .setNotification(notification)
                    .build();

            // Spring Boot le ordena a Firebase que mande el mensaje
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Notificación enviada con éxito: " + response);

        } catch (Exception e) {
            System.out.println("❌ Error al enviar notificación a " + tokenDestino + ": " + e.getMessage());
        }
    }
}