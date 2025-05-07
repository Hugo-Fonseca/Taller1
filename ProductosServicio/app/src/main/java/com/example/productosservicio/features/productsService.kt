package com.example.productosservicio.features

import androidx.lifecycle.viewModelScope
import com.example.productosservicio.models.Product
import com.example.productosservicio.productEndpoint.productEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class productsService:baseService() {
    fun getAllProducts(
        succes: (list: List<Product>) -> Unit,
        error: (data: String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val resp = getRetroFit().create(productEndpoint::class.java).getAllProducts()
                when (val data = resp.body()) {
                    null -> succes(emptyList())
                    else -> succes(data)
                }
            } catch (e: Exception) {
                error("Lo siento, pero no se pudo consumir los servicios")
            }
        }
    }
}