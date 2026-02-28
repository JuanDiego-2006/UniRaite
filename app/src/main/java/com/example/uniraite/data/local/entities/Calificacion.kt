package com.example.uniraite.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "calificaciones",
    foreignKeys = [
        ForeignKey(entity = Usuario::class, parentColumns = ["idUsuario"], childColumns = ["idConductor"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Usuario::class, parentColumns = ["idUsuario"], childColumns = ["idPasajero"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Calificacion(
    @PrimaryKey(autoGenerate = true) val idCalificacion: Int = 0,
    val idConductor: Int,
    val idPasajero: Int,
    val puntuacion: Float,
    val comentario: String?,
    val fecha: String
)