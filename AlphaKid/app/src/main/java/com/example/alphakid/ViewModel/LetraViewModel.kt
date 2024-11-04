package com.example.alphakid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alphakid.models.Letra
import com.example.alphakid.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.alphakid.ViewModel.LetraViewModel;

class LetraViewModel : ViewModel() {

    private val _letras = MutableLiveData<List<Letra>>()
    val letras: LiveData<List<Letra>> get() = _letras

    fun fetchLetras() {
        RetrofitInstance.api.getLetras().enqueue(object : Callback<List<Letra>> {
            override fun onResponse(call: Call<List<Letra>>, response: Response<List<Letra>>) {
                if (response.isSuccessful) {
                    _letras.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Letra>>, t: Throwable) {
                // Manejo de errores
            }
        })
    }
}