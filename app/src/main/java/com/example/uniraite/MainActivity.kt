package com.example.uniraite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uniraite.presentation.screens.*
import com.example.uniraite.presentation.viewmodels.ViajesViewModel
import com.example.uniraite.ui.theme.UniRaiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniRaiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viajesViewModelCompartido: ViajesViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable("forgot_password") { ForgotPasswordScreen(navController) }
                        composable("role_selection") { RoleSelectionScreen(navController) }
                        composable("vehicle_registration") { VehicleRegistrationScreen(navController) }
                        composable("driver_home") { DriverHomeScreen(navController, viajesViewModelCompartido) }
                        composable("edit_vehicle") { EditVehicleScreen(navController) }

                        composable("publish_trip") {
                            PublishTripScreen(
                                onBack = { navController.popBackStack() },
                                onPublishSuccess = { navController.popBackStack() },
                                viajesViewModel = viajesViewModelCompartido
                            )
                        }

                        composable("home") { HomeScreen(navController, viajesViewModelCompartido) }
                        composable("search_results") { SearchResultsScreen(navController, viajesViewModelCompartido) }

                        // 🔥 NUEVA RUTA: Para abrir el historial
                        composable("my_reservations") { MyReservationsScreen(navController, viajesViewModelCompartido) }

                        composable(
                            route = "trip_details/{idViaje}",
                            arguments = listOf(navArgument("idViaje") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getLong("idViaje") ?: 0L
                            TripDetailsScreen(navController, id, viajesViewModelCompartido)
                        }

                        composable("profile") { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}