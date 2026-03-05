package com.example.uniraite.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Text("¿Cómo usarás UniRaite hoy?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                SesionActual.rolUsuario = "ESTUDIANTE"
                navController.navigate("home") { popUpTo("role_selection") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth().height(80.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = "Estudiante", modifier = Modifier.size(32.dp), tint = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Soy Estudiante\n(Buscar Raites)", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                SesionActual.rolUsuario = "CONDUCTOR"
                authViewModel.verificarVehiculo(SesionActual.idUsuario) { vehiculo ->
                    if (vehiculo != null) {
                        SesionActual.idVehiculo = vehiculo.idVehiculo
                        navController.navigate("driver_home")
                    } else {
                        navController.navigate("vehicle_registration")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(80.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen), shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.DirectionsCar, contentDescription = "Conductor", modifier = Modifier.size(32.dp), tint = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Soy Conductor\n(Ofrecer Raites)", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}