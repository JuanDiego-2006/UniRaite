package com.example.uniraite.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniraite.models.Viaje
import com.example.uniraite.api.ApiService
import com.example.uniraite.api.RetrofitClient
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

    init {
        cargarViajesReales()
    }

    fun cargarViajesReales() {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerViajesOrdenados()
                if (response.isSuccessful) {
                    val todosLosViajes = response.body() ?: emptyList()

                    // CORRECCIÓN: Usar Locale.US para que entienda "AM/PM" perfectamente
                    val formato = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
                    val ahora = Calendar.getInstance().time

                    val viajesVigentes = todosLosViajes.filter { viaje ->
                        try {
                            val fechaViaje = formato.parse(viaje.horaSalida)
                            fechaViaje?.after(ahora) == true
                        } catch (e: Exception) {
                            // Cambiamos a true para que, si hay un viaje con formato viejo, no desaparezca
                            true
                        }
                    }

                    _viajes.value = viajesVigentes
                }
            } catch (e: Exception) {
                Log.e("ViajesViewModel", "Error al cargar viajes", e)
            }
        }
    }

    fun cargarViajesPorConductor(idConductor: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerViajesPorConductor(idConductor)
                if (response.isSuccessful) {
                    _misViajes.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ViajesViewModel", "Error al obtener viajes del conductor", e)
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
                } else {
                    onError("Error ${response.code()}: El servidor rechazó los datos.")
                }
            } catch (e: Exception) {
                onError("No se pudo conectar con el servidor.")
            }
        }
    }

    fun apartarLugar(idViaje: Long, idUsuario: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val viajeReservado = _viajes.value.find { it.id == idViaje }
            if (viajeReservado != null) {
                val listaActual = _misReservas.value.toMutableList()
                if (!listaActual.any { it.id == idViaje }) {
                    listaActual.add(viajeReservado)
                    _misReservas.value = listaActual
                }
            }
            onSuccess()
        }
    }
}