package com.example.services_retrofit.services.models

data class LoginResponse(
    val access_token: String,
    val refresh_token: String
)