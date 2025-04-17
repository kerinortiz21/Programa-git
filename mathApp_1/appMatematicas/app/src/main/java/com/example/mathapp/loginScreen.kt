package com.example.mathapp

import android.widget.Toast
import androidx.compose.foundation.background
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

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> onLoginSuccess()
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
    ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium, color = Color(0XFF32CD32))
        Spacer(modifier = Modifier.height(16.dp))

        // email fied
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
        shape = RoundedCornerShape(22.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0XFF32CD32),
                focusedLabelColor = Color(0XFF32CD32)
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(22.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0XFF32CD32),
                focusedLabelColor = Color(0XFF32CD32)
            )

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.login(email, password)


                         },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32CD32))) {
            
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToRegister){
            Text("¿No tienes cuenta? Regístrate")
        }
    }
    }
}
