package com.example.services_retrofit.services.models

data class Product(
    val id: Int,
    val title: String,
    val slug: String,
    val price: Double,
    val description: String,
    val category: Category,
    val images: List<String>
)