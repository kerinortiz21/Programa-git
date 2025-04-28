package com.example.mathapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()

    // 👀 Efecto para manejar el estado de autenticación
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onLoginSuccess()
                viewModel.resetState()
            }

            is AuthState.Error -> {
                val exception = (authState as AuthState.Error).exception

                if (exception is FirebaseAuthException) {
                    when (exception.errorCode) {

                        "ERROR_TOO_MANY_REQUESTS" -> {
                            Toast.makeText(context, "Demasiados intentos. Intenta más tarde.", Toast.LENGTH_LONG).show()
                        }
                        "ERROR_INVALID_CREDENTIAL" -> {
                            Toast.makeText(context, "Email o contraseña incorrectos.", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(context, "Error: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Ocurrió un error: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                }

                viewModel.resetState()
            }

            else -> { /* No hacer nada si está Idle o Loading */ }
        }
    }


    // Design of the login screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF32CD32)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // email fields
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail ->
                    email = newEmail
                    if (newEmail.isNotBlank()) emailError = null
                },
                label = { Text("Correo") },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF32CD32),
                    focusedLabelColor = Color(0xFF32CD32),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                isError = emailError != null
            )
            if (emailError != null) {
                Text(text = emailError!!, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(15.dp))

            // email password
            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = { newPassword ->
                    password = newPassword
                    if (newPassword.isNotBlank()) passwordError = null
                },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF32CD32),
                    focusedLabelColor = Color(0xFF32CD32),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                isError = passwordError != null
            )
            if (passwordError != null) {
                Text(text = passwordError!!, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botton for login
            Button(
                onClick = {
                    var isValid = true
                    val emailRegex = Regex("^[A-Za-z](.*)([@])(.+)(\\.)(.+)")

                    if (email.isBlank()) {
                        emailError = "Escribe tu email."
                        isValid = false
                    } else if (!email.matches(emailRegex)) {
                        emailError = "Formato del correo inválido."
                        isValid = false
                    }

                    if (password.isBlank()) {
                        passwordError = "Escribe tu contraseña."
                        isValid = false
                    }

                    if (isValid) {
                        viewModel.login(email, password)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32CD32))
            ) {
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // register TextButton
            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
