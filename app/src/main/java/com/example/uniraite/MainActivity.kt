package com.example.uniraite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uniraite.ui.theme.UniRaiteTheme
import com.example.uniraite.presentation.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniRaiteTheme {
                val navController = rememberNavController()

                // Definición de todas las rutas de navegación de la App
                NavHost(
                    navController = navController,
                    startDestination = "login" // La app siempre inicia en el Login
                ) {
                    // --- MÓDULO DE AUTENTICACIÓN ---
                    composable("login") {
                        LoginScreen(navController)
                    }
                    composable("register") {
                        RegisterScreen(navController)
                    }
                    composable("forgot_password") {
                        ForgotPasswordScreen(navController)
                    }
                    composable("reset_password/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        ResetPasswordScreen(navController, email)
                    }

                    // --- MÓDULO DE SELECCIÓN DE ROL Y VEHÍCULO ---
                    composable("role_selection") {
                        RoleSelectionScreen(navController)
                    }
                    composable("vehicle_registration") {
                        VehicleRegistrationScreen(navController)
                    }
                    composable("edit_vehicle") {
                        EditVehicleScreen(navController)
                    }

                    // --- MÓDULO DE CONDUCTOR ---
                    composable("driver_home") {
                        DriverHomeScreen(navController)
                    }
                    composable("publish") {
                        PublishTripScreen(
                            onTripPublished = { navController.popBackStack() },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // --- MÓDULO DE ESTUDIANTE (PASAJERO) ---
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("search") {
                        SearchResultsScreen(navController)
                    }
                    composable(
                        route = "trip_details/{idViaje}",
                        arguments = listOf(navArgument("idViaje") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val idViaje = backStackEntry.arguments?.getInt("idViaje") ?: 0
                        TripDetailsScreen(navController, idViaje)
                    }

                    // --- PERFIL (COMPARTIDO) ---
                    composable("profile") {
                        ProfileScreen(navController)
                    }
                }
            }
        }
    }
}
