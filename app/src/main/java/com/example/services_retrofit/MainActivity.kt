package com.example.services_retrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.services_retrofit.services.features.LoginService
import com.example.services_retrofit.services.features.ProductService
import com.example.services_retrofit.services.features.RegisterService
import com.example.services_retrofit.services.models.Category
import com.example.services_retrofit.services.models.CreateUserResponse
import com.example.services_retrofit.services.models.LoginResponse
import com.example.services_retrofit.services.models.Product
import com.example.services_retrofit.ui.theme.ServicesretrofitTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ServicesretrofitTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("categories") {
            ProductsScreen(navController)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    // Variables de estado para los campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Formulario de login
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título del formulario
        Text(text = "Login", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de texto para el correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error (si es necesario)
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de login
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    val loginService = LoginService()

                    // Para pruebas, puedes usar estas credenciales de prueba
                    // email: john@mail.com
                    // password: changeme

                    // Llamada real a la API con Retrofit
                    loginService.login(email, password).enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            isLoading = false
                            if (response.isSuccessful && response.body() != null) {
                                val loginResponse = response.body()!!
                                val token = loginResponse.access_token
                                val refreshToken = loginResponse.refresh_token

                                // Aquí deberías guardar los tokens en SharedPreferences
                                // Ejemplo:
                                // val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                                // sharedPreferences.edit()
                                //     .putString("access_token", token)
                                //     .putString("refresh_token", refreshToken)
                                //     .apply()

                                Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                                navController.navigate("categories") {
                                    // Limpiar el back stack para que el usuario no pueda volver a la pantalla de login
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                errorMessage = "Credenciales incorrectas"
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            isLoading = false
                            errorMessage = "Error de conexión: ${t.message}"
                        }
                    })
                } else {
                    errorMessage = "Por favor, completa todos los campos."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Ingresar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Link para ir a la pantalla de registro
        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "No tienes cuenta? Regístrate aquí",
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Registro", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // Campo para el nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para confirmar contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro
        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if (password == confirmPassword) {
                        isLoading = true
                        val registerService = RegisterService()

                        // Llamada real a la API
                        registerService.register(name, email, password).enqueue(object : Callback<CreateUserResponse> {
                            override fun onResponse(
                                call: Call<CreateUserResponse>,
                                response: Response<CreateUserResponse>
                            ) {
                                isLoading = false
                                if (response.isSuccessful && response.body() != null) {
                                    val user = response.body()!!
                                    Toast.makeText(context, "Registro exitoso para ${user.name}", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login")
                                } else {
                                    errorMessage = "Error al registrar, intenta con otro email"
                                }
                            }

                            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                                isLoading = false
                                errorMessage = "Error de conexión: ${t.message}"
                            }
                        })
                    } else {
                        errorMessage = "Las contraseñas no coinciden"
                    }
                } else {
                    errorMessage = "Por favor, completa todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Link para volver a login
        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿Ya tienes cuenta? Inicia sesión aquí",
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun ProductsScreen(navController: NavController) {
    val context = LocalContext.current
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        val productService = ProductService()
        productService.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                isLoading = false
                if (response.isSuccessful && response.body() != null) {
                    products = response.body()!!
                    // Log para verificar la respuesta
                    Log.d("ProductsScreen", "Products loaded: ${products.size}")
                    products.forEach { product ->
                        Log.d("ProductsScreen", "Product: ${product.title}, Category image: ${product.category.image}")
                    }
                } else {
                    errorMessage = "Error al cargar los productos: ${response.code()}"
                    Log.e("ProductsScreen", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                isLoading = false
                errorMessage = "Error de conexión: ${t.message}"
                Log.e("ProductsScreen", "Network error", t)
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Productos",
                style = MaterialTheme.typography.headlineMedium
            )

            IconButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("categories") { inclusive = true }
                    }
                }
            ) {
                Text("Salir")
            }
        }

        // Mostrar cargando, error o lista de productos
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage.isNotEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            isLoading = true
                            errorMessage = ""
                            val productService = ProductService()
                            productService.getProducts().enqueue(object : Callback<List<Product>> {
                                override fun onResponse(
                                    call: Call<List<Product>>,
                                    response: Response<List<Product>>
                                ) {
                                    isLoading = false
                                    if (response.isSuccessful && response.body() != null) {
                                        products = response.body()!!
                                    } else {
                                        errorMessage = "Error al cargar los productos"
                                    }
                                }

                                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                                    isLoading = false
                                    errorMessage = "Error de conexión: ${t.message}"
                                }
                            })
                        }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            else -> {
                LazyColumn {
                    items(products) { product ->
                        ProductItem(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Log.d("ProductItem", "Cargando imagen desde URL: ${product.category.image}")
            AsyncImage(
                model = product.images[0],
                contentDescription = "${product.category.name} category image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Categoría: ${product.category.name}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Precio: $${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    ServicesretrofitTheme {
        LoginScreen(rememberNavController())
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun RegisterScreenPreview() {
    ServicesretrofitTheme {
        RegisterScreen(rememberNavController())
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun ProductItemPreview() {
    ServicesretrofitTheme {
        // Mock de un producto para el preview
        val mockCategory = Category(
            id = 1,
            name = "Clothes",
            slug = "clothes",
            image = "https://i.imgur.com/QkIa5tT.jpeg",
            creationAt = "2025-05-09T11:34:09.000Z",
            updatedAt = "2025-05-09T11:34:09.000Z"
        )

        val mockProduct = Product(
            id = 10,
            title = "Classic Blue Baseball Cap",
            slug = "classic-blue-baseball-cap",
            price = 86.0,
            description = "Top off your casual look with our Classic Blue Baseball Cap...",
            category = mockCategory,
            images = listOf("https://i.imgur.com/QkIa5tT.jpeg")
        )

        ProductItem(product = mockProduct)
    }
}