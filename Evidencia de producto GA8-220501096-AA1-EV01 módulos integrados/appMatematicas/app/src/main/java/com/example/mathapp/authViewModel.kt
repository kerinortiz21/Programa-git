package com.example.mathapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 📌 Estados de autenticación
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val exception: Exception) : AuthState() // 🔥 Guardamos la EXCEPCIÓN real
}

// 📌 ViewModel para manejar la autenticación
class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // 📌 Función de Login
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading // 🔥 Activamos estado de carga
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception ?: Exception("Error desconocido"))
                }
            }
    }

    // 📌 Función de Registro
    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading // 🔥 Activamos estado de carga
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception ?: Exception("Error desconocido"))
                }
            }
    }

    // 📌 Función para Cerrar Sesión
    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            _authState.value = AuthState.Idle
        }
    }

    // 📌 Función para Resetear Estado
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
