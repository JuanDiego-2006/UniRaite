package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.data.local.entities.Vehiculo
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVehicleScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var placas by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var asientos by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authViewModel.verificarVehiculo(SesionActual.idUsuario) { vehiculo ->
            if (vehiculo != null) {
                marca = vehiculo.marca
                modelo = vehiculo.modelo
                placas = vehiculo.placas
                color = vehiculo.color
                anio = vehiculo.anio
                asientos = vehiculo.numeroAsientos.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Vehículo", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF8F9FA)).padding(20.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = anio, onValueChange = { anio = it }, label = { Text("Año") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = placas, onValueChange = { placas = it }, label = { Text("Placas") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = asientos, onValueChange = { asientos = it }, label = { Text("Asientos") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    val vehiculoEditado = Vehiculo(
                        idVehiculo = SesionActual.idVehiculo,
                        idUsuario = SesionActual.idUsuario,
                        marca = marca, modelo = modelo, anio = anio, placas = placas, color = color,
                        numeroAsientos = asientos.toIntOrNull() ?: 4, fotoVehiculoUri = null, fotoPlacasUri = null, detallesAdicionales = null
                    )
                    authViewModel.registrarVehiculo(vehiculoEditado) {
                        Toast.makeText(context, "Datos del vehículo actualizados", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Guardar Cambios", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}