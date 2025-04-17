package com.example.mathapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var name by remember {mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Registro exitoso. Inicia sesión.", Toast.LENGTH_LONG).show()
                viewModel.resetState()
                onNavigateToLogin()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }
Box(
    modifier = Modifier.fillMaxSize()
        .padding(16.dp)
        .background(Color.White)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro", color = Color(0xFF32CD32), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // name


        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            shape = RoundedCornerShape(22.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0XFF32CD32),
                focusedLabelColor = Color(0XFF32CD32)
            )

            )

        // email field of registerScreen
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            shape = RoundedCornerShape(22.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = Color(0XFF32CD32),
                focusedBorderColor = Color(0XFF32CD32)
            )


        )

        // password field of registerScreen
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(22.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = Color(0XFF32CD32),
                focusedTextColor = Color(0XFF32CD32)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.register(email, password) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32CD32))
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")

              }
         }

    }
}
