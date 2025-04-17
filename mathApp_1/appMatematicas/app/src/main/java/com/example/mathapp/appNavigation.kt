package com.example.mathapp


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mathapp.AuthViewModel
import com.example.mathapp.LoginScreen
import com.example.mathapp.RegisterScreen
import com.example.mathapp.HomeScreen

@Composable
fun AppNavigation(navController: NavHostController, viewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    // Redirige al login después del registro
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}
