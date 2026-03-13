package com.example.uniraite.models

import com.google.gson.annotations.SerializedName

data class Vehiculo(
    val id: Long? = null,
    val marca: String,
    val modelo: String,
    val anio: String,
    val color: String,
    val placas: String,

    // SerializedName asegura que Gson lea el JSON del servidor correctamente
    @SerializedName("numeroAsientos")
    val numeroAsientos: Int,

    @SerializedName("usuarioId")
    val usuarioId: Long,

    // Dejamos estos como nulos por defecto para que no estorben
    val fotoVehiculoUri: String? = null,
    val fotoPlacasUri: String? = null,
    val detallesAdicionales: String? = null
)