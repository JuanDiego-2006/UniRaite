package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.data.local.entities.Vehiculo
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleRegistrationScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var colorAuto by remember { mutableStateOf("") }
    var placas by remember { mutableStateOf("") }
    var asientos by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Vehículo", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ingresa los datos de tu auto", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca (Ej. Nissan)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo (Ej. Versa)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = anio, onValueChange = { anio = it }, label = { Text("Año") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = colorAuto, onValueChange = { colorAuto = it }, label = { Text("Color") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = placas, onValueChange = { placas = it }, label = { Text("Placas") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = asientos, onValueChange = { asientos = it }, label = { Text("Asientos disponibles") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (marca.isNotEmpty() && modelo.isNotEmpty() && anio.isNotEmpty() && colorAuto.isNotEmpty() && placas.isNotEmpty() && asientos.isNotEmpty()) {

                        val nuevoVehiculo = Vehiculo(
                            idUsuario = SesionActual.idUsuario,
                            marca = marca, modelo = modelo, anio = anio, color = colorAuto,
                            placas = placas, numeroAsientos = asientos.toIntOrNull() ?: 4,
                            fotoVehiculoUri = null, fotoPlacasUri = null, detallesAdicionales = null
                        )

                        authViewModel.registrarVehiculo(nuevoVehiculo) { idGenerado ->
                            SesionActual.idVehiculo = idGenerado.toInt()
                            Toast.makeText(context, "Vehículo registrado correctamente", Toast.LENGTH_SHORT).show()
                            navController.navigate("driver_home") { popUpTo("role_selection") { inclusive = true } }
                        }
                    } else {
                        Toast.makeText(context, "Llena todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Guardar y Entrar", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}