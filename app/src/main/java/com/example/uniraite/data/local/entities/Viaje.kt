package com.example.uniraite.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "viajes",
    foreignKeys = [
        ForeignKey(entity = Usuario::class, parentColumns = ["idUsuario"], childColumns = ["idConductor"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Vehiculo::class, parentColumns = ["idVehiculo"], childColumns = ["idVehiculo"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Viaje(
    @PrimaryKey(autoGenerate = true) val idViaje: Int = 0,
    val idConductor: Int,
    val idVehiculo: Int,
    val puntoSalida: String,
    val destino: String,
    val fechaSalida: String,
    val horaSalida: String,
    val cuposTotales: Int,
    val cuposDisponibles: Int,
    val costoPorPersona: Double,
    val descripcionAdicional: String?,
    val puntosIntermedios: String?,
    val estado: String = "ACTIVO"
)