package com.example.uniraite.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehiculos",
    foreignKeys = [
        ForeignKey(entity = Usuario::class, parentColumns = ["idUsuario"], childColumns = ["idUsuario"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Vehiculo(
    @PrimaryKey(autoGenerate = true) val idVehiculo: Int = 0,
    val idUsuario: Int,
    val marca: String,
    val modelo: String,
    val anio: String,
    val color: String,
    val placas: String,
    val numeroAsientos: Int,
    val fotoVehiculoUri: String?,
    val fotoPlacasUri: String?,
    val detallesAdicionales: String?
)