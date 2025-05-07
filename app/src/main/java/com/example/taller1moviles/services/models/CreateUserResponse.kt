package com.example.taller1moviles.services.models

data class CreateUserResponse(
    val email: String,
    val password: String,
    val name: String,
    val avatar: String,
    val role: String,
    val id: Int
)
