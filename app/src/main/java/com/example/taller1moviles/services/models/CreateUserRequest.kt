package com.example.taller1moviles.services.models

data class CreateUserRequest (
    val name: String,
    val email: String,
    val password: String,
    val avatar: String
)