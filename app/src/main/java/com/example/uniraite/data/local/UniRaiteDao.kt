package com.example.uniraite.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uniraite.data.local.entities.Usuario
import com.example.uniraite.data.local.entities.Viaje

@Dao
interface UniRaiteDao {

    // --- USUARIOS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correoInstitucional = :correo AND contrasena = :contrasena")
    suspend fun login(correo: String, contrasena: String): Usuario?

    // NUEVO: Busca si el correo existe
    @Query("SELECT * FROM usuarios WHERE correoInstitucional = :correo")
    suspend fun buscarUsuarioPorCorreo(correo: String): Usuario?

    // NUEVO: Actualiza la contraseña de un usuario específico
    @Query("UPDATE usuarios SET contrasena = :nuevaContrasena WHERE correoInstitucional = :correo")
    suspend fun actualizarContrasena(correo: String, nuevaContrasena: String)

    // --- VIAJES ---
    @Insert
    suspend fun publicarViaje(viaje: Viaje)

    @Query("SELECT * FROM viajes WHERE cuposDisponibles > 0 AND estado = 'ACTIVO'")
    suspend fun obtenerViajesDisponibles(): List<Viaje>
}