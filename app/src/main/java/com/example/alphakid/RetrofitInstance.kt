package com.example.alphakid

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.18.29:8000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)  // Aumenta el tiempo de espera de conexión
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)     // Aumenta el tiempo de espera de lectura
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)    // Aumenta el tiempo de espera de escritura
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}