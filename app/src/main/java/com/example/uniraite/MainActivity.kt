package com.example.uniraite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uniraite.ui.theme.UniRaiteTheme
import com.example.uniraite.presentation.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniRaiteTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("register") { RegisterScreen(navController) }
                    composable("forgot_password") { ForgotPasswordScreen(navController) }
                    composable("reset_password/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        ResetPasswordScreen(navController, email)
                    }

                    // NUEVAS RUTAS DE ROLES
                    composable("role_selection") { RoleSelectionScreen(navController) }
                    composable("vehicle_registration") { VehicleRegistrationScreen(navController) }
                    composable("driver_home") { DriverHomeScreen(navController) }

                    composable("home") { HomeScreen(navController) }
                    composable("search") { SearchResultsScreen(navController) }
                    composable("publish") {
                        PublishTripScreen(
                            onBack = { navController.popBackStack() },
                            onTripPublished = { navController.popBackStack() }
                        )
                    }
                    composable("profile") { ProfileScreen(navController) }
                }
            }
        }
    }
}