package com.example.uniraite.data.local

import androidx.room.*
import com.example.uniraite.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UniRaiteDao {

    // --- GESTIÓN DE USUARIOS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correoInstitucional = :correo AND contrasena = :contrasena")
    suspend fun login(correo: String, contrasena: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correoInstitucional = :correo")
    suspend fun buscarUsuarioPorCorreo(correo: String): Usuario?

    @Query("UPDATE usuarios SET contrasena = :nuevaContrasena WHERE correoInstitucional = :correo")
    suspend fun actualizarContrasena(correo: String, nuevaContrasena: String)

    // Función para guardar permanentemente el contacto de emergencia en la base de datos
    @Query("UPDATE usuarios SET nombreEmergencia = :nombre, telefonoEmergencia = :telefono WHERE idUsuario = :idUsuario")
    suspend fun actualizarContactoEmergencia(idUsuario: Int, nombre: String, telefono: String)

    // --- GESTIÓN DE VEHÍCULOS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarVehiculo(vehiculo: Vehiculo): Long

    @Query("SELECT * FROM vehiculos WHERE idUsuario = :idUsuario LIMIT 1")
    suspend fun obtenerVehiculoPorUsuario(idUsuario: Int): Vehiculo?

    // --- GESTIÓN DE VIAJES ---
    @Insert
    suspend fun publicarViaje(viaje: Viaje)

    @Query("SELECT * FROM viajes WHERE cuposDisponibles > 0 AND estado = 'ACTIVO' ORDER BY horaSalida ASC")
    suspend fun obtenerViajesDisponibles(): List<Viaje>

    @Query("SELECT * FROM viajes WHERE idConductor = :idConductor ORDER BY idViaje DESC")
    fun obtenerViajesPorConductor(idConductor: Int): Flow<List<Viaje>>

    // --- LÓGICA DE RESERVAS (APARTAR LUGAR) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReserva(reserva: Reserva)

    @Query("UPDATE viajes SET cuposDisponibles = cuposDisponibles - 1 WHERE idViaje = :idViaje AND cuposDisponibles > 0")
    suspend fun reducirCupo(idViaje: Int)

    @Query("""
        SELECT viajes.* FROM viajes 
        INNER JOIN reservas ON viajes.idViaje = reservas.idViaje 
        WHERE reservas.idPasajero = :idEstudiante
    """)
    fun obtenerMisViajesReservados(idEstudiante: Int): Flow<List<Viaje>>
}