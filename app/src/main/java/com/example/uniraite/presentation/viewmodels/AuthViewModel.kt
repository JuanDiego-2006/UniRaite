package com.example.uniraite.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniraite.models.Usuario
import com.example.uniraite.models.Vehiculo
import com.example.uniraite.api.ApiService
import com.example.uniraite.api.RetrofitClient
import com.example.uniraite.SesionActual
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.retrofit.create(ApiService::class.java)

    fun registrarUsuario(nombre: String, matricula: String, correo: String, telefono: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val nuevoUsuario = Usuario(
                    nombreCompleto = nombre, matricula = matricula, correoInstitucional = correo,
                    telefono = telefono, contrasena = contrasena
                )
                val response = apiService.registrarUsuario(nuevoUsuario)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error de red", e)
            }
        }
    }

    fun loginUsuarioCompleto(correo: String, contrasena: String, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch {
            try {
                val loginData = Usuario(
                    nombreCompleto = "", correoInstitucional = correo, contrasena = contrasena
                )
                val response = apiService.loginUsuario(loginData)
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun actualizarContrasena(correo: String, nuevaContrasena: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.recuperarContrasena(correo, nuevaContrasena)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun actualizarPerfil(usuarioEditado: Usuario, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val idUsuario = usuarioEditado.id ?: SesionActual.idUsuario.toLong()
                val response = apiService.actualizarUsuario(idUsuario, usuarioEditado)

                if (response.isSuccessful) {
                    val userActualizado = response.body()
                    if (userActualizado != null) {
                        SesionActual.nombreUsuario = userActualizado.nombreCompleto
                        SesionActual.carrera = userActualizado.carrera ?: ""
                        SesionActual.fotoPerfilUrl = userActualizado.foto ?: ""
                    }
                    onSuccess()
                } else {
                    onError("Error al actualizar: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("No se pudo conectar con el servidor.")
            }
        }
    }

    fun verificarVehiculo(idUsuario: Int, onResult: (Vehiculo?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerVehiculoPorUsuario(idUsuario.toLong())
                if (response.isSuccessful) onResult(response.body()) else onResult(null)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun registrarVehiculo(vehiculo: Vehiculo, onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.registrarVehiculo(vehiculo)
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()?.id ?: 0L)
                } else {
                    onError("Error al guardar: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Sin conexión al servidor")
            }
        }
    }

    // --- FUNCIONES AGREGADAS PARA EL PERFIL Y CONTACTO DE EMERGENCIA ---
    fun obtenerUsuarioActual(id: Long, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerPerfil(id)
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun guardarContactoEmergencia(idUsuario: Long, nombre: String, telefono: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.actualizarContactoEmergencia(idUsuario, nombre, telefono)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error del servidor: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Fallo de conexión al guardar contacto")
            }
        }
    }
}