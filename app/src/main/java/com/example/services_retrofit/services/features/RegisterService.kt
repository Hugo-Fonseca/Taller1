package com.example.services_retrofit.services.features

import com.example.services_retrofit.services.endpoints.ApiService
import com.example.services_retrofit.services.models.CreateUserRequest
import com.example.services_retrofit.services.models.CreateUserResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterService {
    private val domainUrl: String = "https://api.escuelajs.co/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(domainUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun register(name: String, email: String, password: String): Call<CreateUserResponse> {
        val avatar = "https://api.lorem.space/image/face?w=640&h=480" // Default avatar
        val createUserRequest = CreateUserRequest(name, email, password, avatar)
        return apiService.createUser(createUserRequest)
    }
}