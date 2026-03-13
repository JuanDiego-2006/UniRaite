package com.example.uniraite.models

data class Viaje(
    val id: Long? = null,
    val puntoSalida: String,
    val destino: String,
    val horaSalida: String,
    val costo: Double,
    val asientosDisponibles: Int,
    val conductorId: Long,
    val conductorNombre: String? = null
)