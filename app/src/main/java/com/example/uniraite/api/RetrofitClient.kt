package com.example.uniraite.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Asegúrate de que esta IP sea la de tu máquina o usa 10.0.2.2 si es el emulador
    private const val BASE_URL = "http://192.168.1.25:8080/api/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
