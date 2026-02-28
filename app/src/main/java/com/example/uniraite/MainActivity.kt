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

                    // 1. Pantalla de Iniciar Sesión
                    composable("login") {
                        LoginScreen(navController)
                    }

                    // 2. Pantalla de Registro
                    composable("register") {
                        RegisterScreen(navController)
                    }

                    // 3. Pantalla de Recuperar Contraseña
                    composable("forgot_password") {
                        ForgotPasswordScreen(navController)
                    }

                    // 4. Pantalla de Cambiar Contraseña (¡NUEVA!)
                    composable("reset_password/{email}") { backStackEntry ->
                        // Recuperamos el correo que se envió desde la pantalla anterior
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        ResetPasswordScreen(navController, email)
                    }

                    // 5. Pantalla Principal (Home)
                    composable("home") {
                        HomeScreen(navController)
                    }

                    // 6. Pantalla de Buscar Viajes
                    composable("search") {
                        SearchResultsScreen(navController)
                    }

                    // 7. Pantalla de Publicar Viaje
                    composable("publish") {
                        PublishTripScreen(
                            onBack = { navController.popBackStack() },
                            onTripPublished = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}