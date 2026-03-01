package com.example.uniraite.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniraite.data.local.UniRaiteDatabase
import com.example.uniraite.data.local.entities.Usuario
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = UniRaiteDatabase.getDatabase(application).uniRaiteDao()

    fun registrarUsuario(nombre: String, matricula: String, correo: String, telefono: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(nombreCompleto = nombre, matricula = matricula, correoInstitucional = correo, telefono = telefono, contrasena = contrasena)
            dao.insertarUsuario(nuevoUsuario)
            onSuccess()
        }
    }

    fun loginUsuario(correo: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val usuarioEncontrado = dao.login(correo, contrasena)
            onResult(usuarioEncontrado != null)
        }
    }

    fun verificarCorreo(correo: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val usuario = dao.buscarUsuarioPorCorreo(correo)
            onResult(usuario != null)
        }
    }

    fun cambiarContrasena(correo: String, nuevaContrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.actualizarContrasena(correo, nuevaContrasena)
            onSuccess()
        }
    }

    // NUEVO: Extrae toda la información del alumno logueado usando su correo
    fun obtenerUsuarioActual(correo: String, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch {
            val usuario = dao.buscarUsuarioPorCorreo(correo)
            onResult(usuario)
        }
    }
}