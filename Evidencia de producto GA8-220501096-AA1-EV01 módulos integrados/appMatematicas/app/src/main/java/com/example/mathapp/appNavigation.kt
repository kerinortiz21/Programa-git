package com.example.mathapp


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mathapp.AuthViewModel
import com.example.mathapp.LoginScreen
import com.example.mathapp.RegisterScreen
import com.example.mathapp.HomeScreen

// Navigation graph
@Composable
fun AppNavigation(navController: NavHostController, viewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            // call login screen
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            // call register screen
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        // delete the register screen from the back stack
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() } // navegate to login
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}
