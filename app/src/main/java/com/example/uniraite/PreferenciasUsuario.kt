package com.example.uniraite

import android.content.Context
import android.content.SharedPreferences

class PreferenciasUsuario(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("UniRaitePrefs", Context.MODE_PRIVATE)

    // Guardar URL
    fun guardarFotoUrl(url: String) {
        preferences.edit().putString("foto_perfil_url", url).apply()
    }

    // Leer URL (Carga cadena vacía si no existe)
    fun obtenerFotoUrl(): String {
        return preferences.getString("foto_perfil_url", "") ?: ""
    }

    // Limpiar (Al cerrar sesión)
    fun limpiarPrefs() {
        preferences.edit().clear().apply()
    }
}