package com.example.productosservicio.productEndpoint

import com.example.productosservicio.models.Product
import retrofit2.http.GET
import retrofit2.Response

interface productEndpoint {
    @GET("/product")
    suspend fun getAllProducts():Response<List<Product>>
}