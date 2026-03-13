package com.example.uniraite.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@Composable
fun RoleSelectionScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val PrimaryBlue = Color(0xFF1565C0)
    val PrimaryGreen = Color(0xFF2E7D32)
    val BackgroundGray = Color(0xFFF8F9FA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿Cómo usarás UniRaite hoy?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Selecciona tu modo de viaje",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(48.dp))

        // Botón Estudiante (Pasajero)
        Button(
            onClick = {
                SesionActual.rolUsuario = "ESTUDIANTE"
                navController.navigate("home") {
                    popUpTo("role_selection") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Soy Pasajero", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Conductor
        Button(
            onClick = {
                SesionActual.rolUsuario = "CONDUCTOR"
                // AQUÍ ESTÁ LA CORRECCIÓN: Verifica si tiene vehículo antes de dejarlo pasar
                authViewModel.verificarVehiculo(SesionActual.idUsuario) { vehiculo ->
                    if (vehiculo != null) {
                        navController.navigate("driver_home") {
                            popUpTo("role_selection") { inclusive = true }
                        }
                    } else {
                        navController.navigate("vehicle_registration")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Soy Conductor", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}