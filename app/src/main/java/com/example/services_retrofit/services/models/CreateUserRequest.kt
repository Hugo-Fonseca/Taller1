package com.example.services_retrofit.services.models

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val avatar: String
)