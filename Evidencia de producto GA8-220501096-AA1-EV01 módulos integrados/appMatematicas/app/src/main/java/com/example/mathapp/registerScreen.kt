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
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    // Variables de estado
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
                viewModel.resetState()
            }

            is AuthState.Error -> {
                val exception = (authState as AuthState.Error).exception
                if (exception is FirebaseAuthException) {
                    when (exception.errorCode) {
                        "ERROR_INVALID_EMAIL" -> {
                            emailError = "El correo electrónico no es válido."
                        }
                        "ERROR_EMAIL_ALREADY_IN_USE" -> {
                            emailError = "Este correo ya está registrado."
                        }
                        "ERROR_WEAK_PASSWORD" -> {
                            passwordError = "La contraseña es demasiado débil. Usa una combinación más fuerte."
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

            else -> {}
        }
    }

    // Design of register screen
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
            Text("Regístrate", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF32CD32))
            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    name = newName
                    if (newName.isNotBlank()) nameError = null
                },
                label = { Text("Nombre") },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                isError = nameError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF32CD32),
                    focusedLabelColor = Color(0xFF32CD32),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            if (nameError != null) {
                Text(nameError!!, color = Color.Red)
            }

            // email field
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail ->
                    email = newEmail
                    if (newEmail.isNotBlank()) emailError = null
                },
                label = { Text("Correo") },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                isError = emailError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF32CD32),
                    focusedLabelColor = Color(0xFF32CD32),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            if (emailError != null) {
                Text(emailError!!, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(15.dp))

            // password field
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
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                isError = passwordError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF32CD32),
                    focusedLabelColor = Color(0xFF32CD32),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            if (passwordError != null) {
                Text(passwordError!!, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(15.dp))

            // confirmation password field
            var confirmPasswordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { newConfirmPassword ->
                    confirmPassword = newConfirmPassword
                    if (newConfirmPassword.isNotBlank()) confirmPasswordError = null
                },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                isError = confirmPasswordError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF32CD32),
                    focusedLabelColor = Color(0xFF32CD32),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            if (confirmPasswordError != null) {
                Text(confirmPasswordError!!, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button of register
            Button(
                onClick = {
                    var isValid = true

                    if (name.isBlank()) {
                        nameError = "Escribe tu nombre"
                        isValid = false
                    }
                    val emailRegex = Regex("^[A-Za-z](.*)([@])(.+)(\\.)(.+)")

                    if (email.isBlank()) {
                        emailError = "Escribe tu email"
                        isValid = false
                    } else if (!email.matches(emailRegex)) {
                        emailError = "Formato del correo inválido."
                        isValid = false
                    }

                    if (password.isBlank()) {
                        passwordError = "Escribe tu contraseña"
                        isValid = false
                    }

                    if (confirmPassword.isBlank()) {
                        confirmPasswordError = "Confirma tu contraseña"
                        isValid = false
                    } else if (password != confirmPassword) {
                        confirmPasswordError = "Las contraseñas no coinciden"
                        isValid = false
                    }

                    if (isValid) {
                        viewModel.register(email, password)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32CD32))
            ) {
                Text("Regístrate")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // TextButton to login
            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
