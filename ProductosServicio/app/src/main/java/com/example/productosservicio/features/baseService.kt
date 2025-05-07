package com.example.productosservicio.features

import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class baseService:ViewModel(){
private val domainUrl:String="https://api.escuelajs.co/api/v1/products"
    public fun getRetroFit(): Retrofit{
    return Retrofit.Builder()
        .baseUrl(this.domainUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    }
}