package com.example.uniraite.api

import com.example.uniraite.models.Usuario
import com.example.uniraite.models.Viaje
import com.example.uniraite.models.Vehiculo
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.ResponseBody


data class ReservaBackend(
    val id: Long? = null,
    val viajeId: Long,
    val pasajeroId: Long,
    val estado: String,
    val fechaReserva: String? = null,
    val viaje: Viaje? = null
)

data class Resena(
    val id: Long? = null,
    val viajeId: Long,
    val evaluadorId: Long,
    val evaluadoId: Long,
    val calificacion: Int,
    val comentario: String
)

interface ApiService {
    @POST("usuarios/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("usuarios/login")
    suspend fun loginUsuario(@Body usuario: Usuario): Response<Usuario>

    @GET("usuarios/{id}")
    suspend fun obtenerPerfil(@Path("id") id: Long): Response<Usuario>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Long, @Body usuario: Usuario): Response<Usuario>

    @POST("usuarios/recuperar")
    suspend fun recuperarContrasena(@Query("correo") correo: String, @Query("nuevaContrasena") nuevaContrasena: String): Response<Void>

    @PUT("usuarios/{id}/contacto")
    suspend fun actualizarContactoEmergencia(@Path("id") id: Long, @Query("nombre") nombreContacto: String, @Query("telefono") telefonoContacto: String): Response<Usuario>

    @GET("viajes")
    suspend fun obtenerViajesOrdenados(): Response<List<Viaje>>

    @GET("viajes/conductor/{id}")
    suspend fun obtenerViajesPorConductor(@Path("id") id: Long): Response<List<Viaje>>

    @POST("viajes")
    suspend fun crearViaje(@Body viaje: Viaje): Response<Viaje>

    @PUT("viajes/{id}")
    suspend fun editarViaje(@Path("id") id: Long, @Body viaje: Viaje): Response<Viaje>

    @DELETE("viajes/{id}")
    suspend fun eliminarViaje(@Path("id") id: Long): Response<Void>

    @GET("vehiculos/usuario/{idUsuario}")
    suspend fun obtenerVehiculoPorUsuario(@Path("idUsuario") idUsuario: Long): Response<Vehiculo>

    @POST("vehiculos")
    suspend fun registrarVehiculo(@Body vehiculo: Vehiculo): Response<Vehiculo>

    @POST("reservas")
    suspend fun crearReserva(@Body reserva: ReservaBackend): Response<ReservaBackend>

    @GET("reservas/historial/{id}")
    suspend fun obtenerReservasPorPasajero(@Path("id") id: Long): Response<List<ReservaBackend>>

    @POST("resenas")
    suspend fun enviarResena(@Body resena: Resena): Response<Resena>

    // 🔥 NUEVA RUTA: Para obtener el promedio de estrellas 🔥
    @GET("resenas/promedio/{usuarioId}")
    suspend fun obtenerPromedioEstrellas(@Path("usuarioId") usuarioId: Long): Response<Double>

    @PUT("usuarios/{id}/token")
    suspend fun actualizarTokenFirebase(
        @Path("id") idUsuario: Long,
        @Query("token") token: String
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("api/usuarios/actualizar-token")
    suspend fun actualizarTokenFCM(
        @Field("usuarioId") usuarioId: Long,
        @Field("fcmToken") fcmToken: String
    ): Response<Void>
}