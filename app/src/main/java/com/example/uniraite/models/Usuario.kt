package com.example.uniraite.models

data class Usuario(
    val id: Long? = null,
    val nombreCompleto: String,
    val matricula: String = "",
    val correoInstitucional: String,
    val telefono: String = "",
    val contrasena: String = "",
    val rol: String = "ESTUDIANTE",
    val carrera: String? = null,
    val foto: String? = null,
    val nombreEmergencia: String? = null,
    val telefonoEmergencia: String? = null
)