package com.example.uniraite.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservas",
    foreignKeys = [
        ForeignKey(entity = Viaje::class, parentColumns = ["idViaje"], childColumns = ["idViaje"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Usuario::class, parentColumns = ["idUsuario"], childColumns = ["idPasajero"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Reserva(
    @PrimaryKey(autoGenerate = true) val idReserva: Int = 0,
    val idViaje: Int,
    val idPasajero: Int,
    val estado: String = "PENDIENTE",
    val fechaReserva: String
)


//asientos reservados
//id reserva, id, viaje, id pasajero,, ARS