package com.example.taller1moviles.services.features

import com.example.taller1moviles.services.endpoints.ApiService
import com.example.taller1moviles.services.models.LoginRequest
import com.example.taller1moviles.services.models.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginService {
    private val domainUrl: String = "https://fakeapi.platzi.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(domainUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun login(email: String, password: String): Call<LoginResponse> {
        val loginRequest = LoginRequest(email, password)
        return apiService.login(loginRequest)
    }
}
