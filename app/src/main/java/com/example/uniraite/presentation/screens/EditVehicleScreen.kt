package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.uniraite.models.Vehiculo
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVehicleScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var placas by remember { mutableStateOf("") }
    var asientos by remember { mutableStateOf("") }

    var idVehiculoNube by remember { mutableStateOf<Long?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargamos los datos actuales de la nube
    LaunchedEffect(Unit) {
        try {
            authViewModel.verificarVehiculo(SesionActual.idUsuario) { vehiculo ->
                if (vehiculo != null) {
                    idVehiculoNube = vehiculo.id
                    marca = vehiculo.marca ?: ""
                    modelo = vehiculo.modelo ?: ""
                    anio = vehiculo.anio ?: ""
                    placas = vehiculo.placas ?: ""
                    color = vehiculo.color ?: ""
                    asientos = vehiculo.numeroAsientos.toString()
                } else {
                    Toast.makeText(context, "No se encontró el vehículo", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al cargar: ${e.message}", Toast.LENGTH_SHORT).show()
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Vehículo", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00965E))
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF00965E))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = anio, onValueChange = { anio = it }, label = { Text("Año") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = placas, onValueChange = { placas = it.uppercase() }, label = { Text("Placas") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = asientos, onValueChange = { asientos = it }, label = { Text("Asientos") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val vehiculoEditado = Vehiculo(
                            id = idVehiculoNube,
                            marca = marca,
                            modelo = modelo,
                            anio = anio,
                            color = color,
                            placas = placas,
                            numeroAsientos = asientos.toIntOrNull() ?: 4,
                            usuarioId = SesionActual.idUsuario.toLong()
                        )

                        authViewModel.registrarVehiculo(
                            vehiculo = vehiculoEditado,
                            onSuccess = {
                                Toast.makeText(context, "Vehículo actualizado", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            },
                            onError = { mensaje ->
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00965E))
                ) {
                    Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}