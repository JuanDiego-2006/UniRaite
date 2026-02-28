package com.example.uniraite.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reportes",
    foreignKeys = [
        ForeignKey(entity = Usuario::class, parentColumns = ["idUsuario"], childColumns = ["idReportador"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Usuario::class, parentColumns = ["idUsuario"], childColumns = ["idReportado"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Reporte(
    @PrimaryKey(autoGenerate = true) val idReporte: Int = 0,
    val idReportador: Int,
    val idReportado: Int,
    val motivo: String,
    val descripcion: String,
    val estado: String = "PENDIENTE",
    val fechaReporte: String
)