package com.example.uniraite.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.uniraite.MainActivity
import com.example.uniraite.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object NotificacionesLocales {

    fun enviarNotificacionInmediata(context: Context, titulo: String, mensaje: String) {
        val channelId = "uniraite_local_notifications"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios UniRaite",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones locales de la app"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Tu icono
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun programarNotificacionFutura(context: Context, tiempoEnMilisegundos: Long, titulo: String, mensaje: String, idAlarma: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ViajeReceiver::class.java).apply {
            putExtra("titulo", titulo)
            putExtra("mensaje", mensaje)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, idAlarma, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, tiempoEnMilisegundos, pendingIntent)
        } catch (e: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, tiempoEnMilisegundos, pendingIntent)
        }
    }

    fun programarAlerta15MinutosAntes(context: Context, fechaHoraString: String, destino: String) {
        try {
            val formato = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
            val fechaViaje = formato.parse(fechaHoraString)

            if (fechaViaje != null) {
                val calendar = Calendar.getInstance()
                calendar.time = fechaViaje
                calendar.add(Calendar.MINUTE, -15) // Restamos 15 minutos

                val tiempoAlarma = calendar.timeInMillis
                val ahora = System.currentTimeMillis()

                if (tiempoAlarma > ahora) {
                    programarNotificacionFutura(
                        context = context,
                        tiempoEnMilisegundos = tiempoAlarma,
                        titulo = "⏰ ¡Prepárate para salir!",
                        mensaje = "Tu UniRaite a $destino sale en 15 minutos. ¡No llegues tarde!",
                        idAlarma = fechaHoraString.hashCode()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}