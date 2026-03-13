package com.example.uniraite.api

import com.example.uniraite.models.Usuario
import com.example.uniraite.models.Vehiculo
import com.example.uniraite.models.Viaje
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // --- USUARIOS ---
    @POST("usuarios/registro")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("usuarios/login")
    suspend fun loginUsuario(@Body loginData: Usuario): Response<Usuario>

    @PUT("usuarios/recuperar-contrasena")
    suspend fun recuperarContrasena(
        @Query("correo") correo: String,
        @Query("nuevaContrasena") nuevaContrasena: String
    ): Response<Unit>

    @GET("usuarios/{id}")
    suspend fun obtenerPerfil(@Path("id") id: Long): Response<Usuario>

    @PUT("usuarios/{id}/contacto-emergencia")
    suspend fun actualizarContactoEmergencia(
        @Path("id") id: Long,
        @Query("nombreContacto") nombreContacto: String,
        @Query("telefonoContacto") telefonoContacto: String
    ): Response<Unit>

    // --- VIAJES ---
    @GET("viajes")
    suspend fun obtenerViajesOrdenados(): Response<List<Viaje>>

    @POST("viajes")
    suspend fun crearViaje(@Body viaje: Viaje): Response<Viaje>

    @PUT("viajes/{id}")
    suspend fun actualizarViaje(@Path("id") id: Long, @Body viaje: Viaje): Response<Viaje>

    @DELETE("viajes/{id}")
    suspend fun eliminarViaje(@Path("id") id: Long): Response<Unit>

    @GET("viajes/conductor/{id}")
    suspend fun obtenerViajesPorConductor(@Path("id") id: Long): Response<List<Viaje>>

    // --- VEHÍCULOS ---
    @POST("vehiculos")
    suspend fun registrarVehiculo(@Body vehiculo: Vehiculo): Response<Vehiculo>

    @GET("vehiculos/usuario/{idUsuario}")
    suspend fun obtenerVehiculoPorUsuario(@Path("idUsuario") idUsuario: Long): Response<Vehiculo>
}
