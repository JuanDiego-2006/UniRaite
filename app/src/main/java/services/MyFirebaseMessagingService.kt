package com.example.uniraite.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.uniraite.MainActivity
import com.example.uniraite.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_UNIRAITE", "NUEVO TOKEN GENERADO: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Log para depuración
        Log.d("FCM_UNIRAITE", "Mensaje recibido de: ${remoteMessage.from}")

        // Si el mensaje trae una notificación, la mostramos visualmente
        remoteMessage.notification?.let {
            Log.d("FCM_UNIRAITE", "Título: ${it.title} | Cuerpo: ${it.body}")
            mostrarNotificacion(it.title ?: "UniRaite", it.body ?: "")
        }
    }

    private fun mostrarNotificacion(titulo: String, mensaje: String) {
        val channelId = "uniraite_notifications_v2"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 🔥 CONFIGURACIÓN PARA ANDROID 8.0 O SUPERIOR (CREAR CANAL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alertas UniRaite",
                NotificationManager.IMPORTANCE_HIGH // IMPORTANCIA ALTA = VENTANA EMERGENTE
            ).apply {
                description = "Notificaciones de viajes y seguridad de UniRaite"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Acción al tocar la notificación (abre la app)
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // 🔥 CONSTRUIR LA NOTIFICACIÓN VISUAL
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de que este icono exista
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // ALTA PRIORIDAD PARA QUE FLOTE
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        // Lanzar la notificación
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}