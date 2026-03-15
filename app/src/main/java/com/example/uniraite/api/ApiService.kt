package com.example.uniraite.api

import com.example.uniraite.models.Usuario
import com.example.uniraite.models.Viaje
import com.example.uniraite.models.Vehiculo
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("usuarios/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("usuarios/login")
    suspend fun loginUsuario(@Body usuario: Usuario): Response<Usuario>

    @GET("usuarios/{id}")
    suspend fun obtenerPerfil(@Path("id") id: Long): Response<Usuario>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Long,
        @Body usuario: Usuario
    ): Response<Usuario>

    @POST("usuarios/recuperar")
    suspend fun recuperarContrasena(
        @Query("correo") correo: String,
        @Query("nuevaContrasena") nuevaContrasena: String
    ): Response<Void>

    @PUT("usuarios/{id}/contacto")
    suspend fun actualizarContactoEmergencia(
        @Path("id") id: Long,
        @Query("nombre") nombreContacto: String,
        @Query("telefono") telefonoContacto: String
    ): Response<Usuario>

    @GET("viajes")
    suspend fun obtenerViajesOrdenados(): Response<List<Viaje>>

    @GET("viajes/conductor/{id}")
    suspend fun obtenerViajesPorConductor(@Path("id") id: Long): Response<List<Viaje>>

    @POST("viajes")
    suspend fun crearViaje(@Body viaje: Viaje): Response<Viaje>

    // --- NUEVAS FUNCIONES CRUD PARA VIAJES ---
    @PUT("viajes/{id}")
    suspend fun editarViaje(@Path("id") id: Long, @Body viaje: Viaje): Response<Viaje>

    @DELETE("viajes/{id}")
    suspend fun eliminarViaje(@Path("id") id: Long): Response<Void>

    @GET("vehiculos/usuario/{idUsuario}")
    suspend fun obtenerVehiculoPorUsuario(@Path("idUsuario") idUsuario: Long): Response<Vehiculo>

    @POST("vehiculos")
    suspend fun registrarVehiculo(@Body vehiculo: Vehiculo): Response<Vehiculo>
}