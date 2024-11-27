package com.example.alphakid

import retrofit2.http.GET

interface ApiService {
    @GET("api/palabras")
    suspend fun getPalabras(): List<Palabra>
}