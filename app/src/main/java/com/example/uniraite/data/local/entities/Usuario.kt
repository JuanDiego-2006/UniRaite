package com.example.uniraite.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val idUsuario: Int = 0,
    val nombreCompleto: String,
    val matricula: String,
    val correoInstitucional: String,
    val telefono: String,
    val contrasena: String,
    val rol: String = "ESTUDIANTE",
    val estadoValidacion: String = "PENDIENTE",
    // Campos necesarios para el contacto de emergencia
    val nombreEmergencia: String? = null,
    val telefonoEmergencia: String? = null
)