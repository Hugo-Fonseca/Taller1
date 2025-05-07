package com.example.productosservicio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.productosservicio.features.productsService
import com.example.productosservicio.models.Product
import com.example.productosservicio.ui.theme.ProductosServicioTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    private val productsService by lazy { productsService() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
        var product by remember {
        mutableStateOf<List<Product>>(emptyList())
        }
            this.productsService.getAllProducts(
                succes = {
                    list -> System.out.println(list.size)
                    System.out.println(list.get(0).title)
                    product = list
                },
                error = {
                    data -> System.out.println(data)
                }
            )
            LazyColumn {
            items(product){
                Text(text = it.title)
            }
            }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    data class Prodts(var name: String, var Index: Int)

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProductosServicioTheme {
        Greeting("Android")
    }
}