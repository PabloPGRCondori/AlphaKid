package com.example.alphakid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alphakid.models.Palabra
import com.example.alphakid.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PalabraViewModel : ViewModel() {

    private val _palabras = MutableLiveData<List<Palabra>>()
    val palabras: LiveData<List<Palabra>> get() = _palabras

    fun fetchPalabras() {
        RetrofitInstance.api.getPalabras().enqueue(object : Callback<List<Palabra>> {
            override fun onResponse(call: Call<List<Palabra>>, response: Response<List<Palabra>>) {
                if (response.isSuccessful) {
                    _palabras.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Palabra>>, t: Throwable) {
                // Manejo de errores
            }
        })
    }
}