package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uniraite.SesionActual
import com.example.uniraite.data.local.entities.Viaje
import com.example.uniraite.presentation.viewmodels.ViajesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishTripScreen(
    onTripPublished: () -> Unit,
    onBack: () -> Unit,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("UPChiapas") }
    var hora by remember { mutableStateOf("") }
    var cupos by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Nuevo Viaje") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Detalles de la Ruta", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(value = origen, onValueChange = { origen = it }, label = { Text("Punto de Partida") }, leadingIcon = { Icon(Icons.Filled.Place, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = destino, onValueChange = { destino = it }, label = { Text("Destino") }, leadingIcon = { Icon(Icons.Filled.DirectionsCar, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = hora, onValueChange = { hora = it }, label = { Text("Hora de Salida") }, leadingIcon = { Icon(Icons.Filled.Schedule, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = cupos, onValueChange = { cupos = it }, label = { Text("Cupos") }, leadingIcon = { Icon(Icons.Filled.Group, null) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Costo") }, leadingIcon = { Icon(Icons.Filled.AttachMoney, null) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (origen.isBlank() || hora.isBlank() || cupos.isBlank() || precio.isBlank()) {
                        Toast.makeText(context, "Por favor completa todos los datos", Toast.LENGTH_SHORT).show()
                    } else {
                        val nuevoViaje = Viaje(
                            idConductor = SesionActual.idUsuario,
                            idVehiculo = SesionActual.idVehiculo,
                            puntoSalida = origen,
                            destino = destino,
                            fechaSalida = "Hoy",
                            horaSalida = hora,
                            cuposTotales = cupos.toIntOrNull() ?: 4,
                            cuposDisponibles = cupos.toIntOrNull() ?: 4,
                            costoPorPersona = precio.toDoubleOrNull() ?: 0.0,
                            descripcionAdicional = null,
                            puntosIntermedios = null,
                            estado = "ACTIVO"
                        )
                        viajesViewModel.publicarNuevoViaje(nuevoViaje) {
                            Toast.makeText(context, "¡Viaje Publicado Exitosamente!", Toast.LENGTH_SHORT).show()
                            onTripPublished()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Publicar Viaje", fontSize = 18.sp)
            }
        }
    }
}