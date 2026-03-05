package com.example.uniraite.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniraite.data.local.UniRaiteDatabase
import com.example.uniraite.data.local.entities.Viaje
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
            val listaDesdeDB = dao.obtenerViajesDisponibles()
            _viajes.value = listaDesdeDB.sortedBy { it.horaSalida }
        }
    }

    fun publicarNuevoViaje(viaje: Viaje, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.publicarViaje(viaje)
            cargarViajesReales()
            onSuccess()
        }
    }
}