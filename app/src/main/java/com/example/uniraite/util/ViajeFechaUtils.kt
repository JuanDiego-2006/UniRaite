package com.example.uniraite.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ViajeFechaUtils {

    private val formatosCombinados: List<SimpleDateFormat> = listOf(
        SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US),
        SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.US),
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US),
        SimpleDateFormat("dd/MM/yyyy H:mm", Locale.US)
    )

    /**
     * Parsea [horaSalida] tal como se guarda al publicar: "dd/MM/yyyy h:mm AM/PM".
     */
    fun parsearHoraSalida(fechaString: String?): Date? {
        if (fechaString.isNullOrBlank()) return null
        val limpia = fechaString.uppercase(Locale.ROOT)
            .replace(".", "")
            .replace("  ", " ")
            .trim()
        for (f in formatosCombinados) {
            try {
                f.isLenient = false
                val d = f.parse(limpia) ?: continue
                return d
            } catch (_: Exception) {
                continue
            }
        }
        return null
    }

    /**
     * Valida que fecha (dd/MM/yyyy) + hora (12h con AM/PM) sea estrictamente posterior a ahora.
     */
    fun esSalidaEnElFuturo(fechaDdMmYyyy: String, hora12h: String): Boolean {
        val combinada = "${fechaDdMmYyyy.trim()} ${hora12h.trim()}".trim()
        val salida = parsearHoraSalida(combinada) ?: return false
        return salida.after(Date())
    }

    fun mensajeValidacionSalida(fechaDdMmYyyy: String, hora12h: String): String? {
        if (fechaDdMmYyyy.isBlank() || hora12h.isBlank()) return "Selecciona fecha y hora de salida."
        if (!esSalidaEnElFuturo(fechaDdMmYyyy, hora12h)) {
            return "La fecha y hora del viaje deben ser posteriores al momento actual."
        }
        return null
    }
}
