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
import androidx.compose.material.icons.filled.*
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
import com.example.uniraite.models.Vehiculo
import com.example.uniraite.presentation.viewmodels.AuthViewModel

private val GreenPrimary = Color(0xFF00965E)
private val BluePrimary = Color(0xFF1565C0)
private val BackgroundGray = Color(0xFFF8F9FA)
private val DividerColor = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleRegistrationScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var marca    by remember { mutableStateOf("") }
    var modelo   by remember { mutableStateOf("") }
    var anio     by remember { mutableStateOf("") }
    var color    by remember { mutableStateOf("") }
    var placas   by remember { mutableStateOf("") }
    var asientos by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Vehículo", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Banner azul informativo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.DirectionsCar, null, tint = BluePrimary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Registra tu vehículo para poder publicar viajes",
                        fontSize = 13.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Campos del formulario
            VehicleField("Marca", marca, { marca = it }, "Ej: Toyota, Nissan...")
            VehicleField("Modelo", modelo, { modelo = it }, "Ej: Corolla, Versa...")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) {
                    VehicleField("Año", anio, { if (it.length <= 4) anio = it }, "2022", KeyboardType.Number)
                }
                Column(Modifier.weight(1f)) {
                    VehicleField("Color", color, { color = it }, "Ej: Gris, Blanco...")
                }
            }

            VehicleField("Placas", placas, { placas = it.uppercase() }, "ABC-123-D")
            VehicleField("Asientos disponibles", asientos, { asientos = it }, "1-4", KeyboardType.Number)

            Spacer(Modifier.height(20.dp))

            // Botón de Registro
            Button(
                onClick = {
                    if (marca.isNotBlank() && modelo.isNotBlank() && anio.isNotBlank() && placas.isNotBlank() && asientos.isNotBlank()) {
                        val vehiculoNube = Vehiculo(
                            marca = marca,
                            modelo = modelo,
                            anio = anio,
                            color = color,
                            placas = placas,
                            numeroAsientos = asientos.toIntOrNull() ?: 4,
                            usuarioId = SesionActual.idUsuario.toLong()
                        )

                        authViewModel.registrarVehiculo(
                            vehiculo = vehiculoNube,
                            onSuccess = {
                                Toast.makeText(context, "¡Vehículo registrado con éxito!", Toast.LENGTH_SHORT).show()
                                navController.navigate("driver_home") {
                                    popUpTo("vehicle_registration") { inclusive = true }
                                }
                            },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                        )
                    } else {
                        Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Completar Registro", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun VehicleField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 13.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = DividerColor,
                unfocusedContainerColor = BackgroundGray
            )
        )
    }
}