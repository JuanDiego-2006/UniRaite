package com.example.uniraite.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniraite.models.Viaje
import com.example.uniraite.api.ApiService
import com.example.uniraite.api.RetrofitClient
import com.example.uniraite.api.ReservaBackend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ViajesViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.retrofit.create(ApiService::class.java)

    private val _viajes = MutableStateFlow<List<Viaje>>(emptyList())
    val viajes: StateFlow<List<Viaje>> = _viajes

    private val _misViajes = MutableStateFlow<List<Viaje>>(emptyList())
    val misViajes: StateFlow<List<Viaje>> = _misViajes

    private val _misReservas = MutableStateFlow<List<Viaje>>(emptyList())
    val misReservas: StateFlow<List<Viaje>> = _misReservas

    private val _historialReservas = MutableStateFlow<List<ReservaBackend>>(emptyList())
    val historialReservas: StateFlow<List<ReservaBackend>> = _historialReservas

    init {
        cargarViajesReales()
    }

    fun cargarViajesReales() {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerViajesOrdenados()
                if (response.isSuccessful) {
                    val todosLosViajes = response.body() ?: emptyList()
                    val formato = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
                    val ahora = Calendar.getInstance().time

                    val viajesVigentes = todosLosViajes.filter { viaje ->
                        try {
                            val fechaViaje = formato.parse(viaje.horaSalida)
                            fechaViaje?.after(ahora) == true
                        } catch (e: Exception) { true }
                    }
                    _viajes.value = viajesVigentes
                }
            } catch (e: Exception) { Log.e("ViajesViewModel", "Error al cargar viajes", e) }
        }
    }

    fun cargarViajesPorConductor(idConductor: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerViajesPorConductor(idConductor)
                if (response.isSuccessful) {
                    _misViajes.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) { Log.e("ViajesViewModel", "Error al obtener viajes del conductor", e) }
        }
    }

    fun cargarHistorialReservas(idPasajero: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerReservasPorPasajero(idPasajero)
                if (response.isSuccessful) {
                    _historialReservas.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ViajesViewModel", "Error al cargar historial", e)
            }
        }
    }

    fun publicarNuevoViaje(viaje: Viaje, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.crearViaje(viaje)
                if (response.isSuccessful) {
                    cargarViajesReales()
                    cargarViajesPorConductor(viaje.conductorId)
                    onSuccess()
                } else onError("Error ${response.code()}")
            } catch (e: Exception) { onError("No se pudo conectar.") }
        }
    }

    fun apartarLugar(idViaje: Long, idUsuario: Int, onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val nuevaReserva = ReservaBackend(viajeId = idViaje, pasajeroId = idUsuario.toLong(), estado = "CONFIRMADA")
                val response = apiService.crearReserva(nuevaReserva)
                if (response.isSuccessful) {
                    cargarViajesReales()
                    cargarHistorialReservas(idUsuario.toLong())
                    val viajeReservado = _viajes.value.find { it.id == idViaje }
                    if (viajeReservado != null) {
                        val listaActual = _misReservas.value.toMutableList()
                        if (!listaActual.any { it.id == idViaje }) {
                            listaActual.add(viajeReservado)
                            _misReservas.value = listaActual
                        }
                    }
                    onSuccess()
                } else onError("No se pudo realizar la reserva")
            } catch (e: Exception) { onError("Error de conexión") }
        }
    }

    // 🔥 REACTIVIDAD AL ELIMINAR: La tarjeta desaparece al instante de la pantalla
    fun eliminarViaje(idViaje: Long, idConductor: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.eliminarViaje(idViaje)
                if (response.isSuccessful) {
                    // Actualiza la lista del conductor borrando el elemento
                    _misViajes.value = _misViajes.value.filter { it.id != idViaje }
                    // Actualiza la lista general
                    _viajes.value = _viajes.value.filter { it.id != idViaje }
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("ViajesViewModel", "Error al eliminar", e)
            }
        }
    }

    // 🔥 REACTIVIDAD AL EDITAR: La tarjeta muestra los nuevos datos al instante
    fun editarViaje(idViaje: Long, viajeEditado: Viaje, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.editarViaje(idViaje, viajeEditado)
                if (response.isSuccessful) {
                    // Reemplaza el viaje viejo con la nueva información en la lista del conductor
                    _misViajes.value = _misViajes.value.map {
                        if (it.id == idViaje) viajeEditado else it
                    }
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("ViajesViewModel", "Error al editar", e)
            }
        }
    }

    fun enviarCalificacion(resena: com.example.uniraite.api.Resena, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.enviarResena(resena)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Ya calificaste este viaje o hubo un error.")
                }
            } catch (e: Exception) {
                onError("Sin conexión al servidor.")
            }
        }
    }
}