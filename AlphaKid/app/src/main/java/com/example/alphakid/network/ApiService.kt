package com.example.alphakid.network

import com.example.alphakid.models.Letra
import com.example.alphakid.models.Palabra
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("letras/")
    fun getLetras(): Call<List<Letra>>

    @GET("palabras/")
    fun getPalabras(): Call<List<Palabra>>
}