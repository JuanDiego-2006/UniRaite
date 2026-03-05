package com.example.uniraite.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniraite.data.local.UniRaiteDatabase
import com.example.uniraite.data.local.entities.Viaje
import com.example.uniraite.data.local.entities.Reserva
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViajesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = UniRaiteDatabase.getDatabase(application).uniRaiteDao()

    private val _viajes = MutableStateFlow<List<Viaje>>(emptyList())
    val viajes: StateFlow<List<Viaje>> = _viajes

    init {
        cargarViajesReales()
    }

    fun cargarViajesReales() {
        viewModelScope.launch {
            _viajes.value = dao.obtenerViajesDisponibles()
        }
    }

    fun publicarNuevoViaje(viaje: Viaje, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.publicarViaje(viaje)
            cargarViajesReales()
            onSuccess()
        }
    }

    fun obtenerViajesPorConductor(idConductor: Int): Flow<List<Viaje>> {
        return dao.obtenerViajesPorConductor(idConductor)
    }

    // NUEVO: Función para apartar lugar
    fun apartarLugar(idViaje: Int, idEstudiante: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.reducirCupo(idViaje)
            val nuevaReserva = Reserva(
                idViaje = idViaje,
                idPasajero = idEstudiante,
                fechaReserva = "Hoy",
                estado = "CONFIRMADO"
            )
            dao.insertarReserva(nuevaReserva)
            cargarViajesReales() // Refrescar lista general
            onSuccess()
        }
    }

    fun obtenerMisReservas(idEstudiante: Int): Flow<List<Viaje>> {
        return dao.obtenerMisViajesReservados(idEstudiante)
    }
}