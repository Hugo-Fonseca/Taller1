package com.example.services_retrofit.services.endpoints

import com.example.services_retrofit.services.models.CreateUserRequest
import com.example.services_retrofit.services.models.CreateUserResponse
import com.example.services_retrofit.services.models.LoginRequest
import com.example.services_retrofit.services.models.LoginResponse
import com.example.services_retrofit.services.models.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/v1/users")
    fun createUser(@Body createUserRequest: CreateUserRequest): Call<CreateUserResponse>

    @GET("api/v1/products")
    fun getProducts(): Call<List<Product>>
}