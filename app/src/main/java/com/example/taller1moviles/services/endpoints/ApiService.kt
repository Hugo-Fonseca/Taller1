package com.example.taller1moviles.services.endpoints

import com.example.taller1moviles.services.models.CreateUserRequest
import com.example.taller1moviles.services.models.CreateUserResponse
import com.example.taller1moviles.services.models.LoginRequest
import com.example.taller1moviles.services.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/v1/users/")
    fun createUser(@Body createUserRequest: CreateUserRequest): Call<CreateUserResponse>
}