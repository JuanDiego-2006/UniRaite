package com.example.uniraite.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ViajeReceiver : BroadcastReceiver() {
    // Esta función se ejecuta mágicamente cuando el reloj llega a la hora programada
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo") ?: "¡Tu viaje se acerca!"
        val mensaje = intent.getStringExtra("mensaje") ?: "Faltan 15 minutos para salir."

        Log.d("ALARMAS_UNIRAITE", "¡Alarma disparada! Mostrando notificación...")

        // Reutilizamos la función que ya tenías para mostrar el banner
        NotificacionesLocales.enviarNotificacionInmediata(context, titulo, mensaje)
    }
}