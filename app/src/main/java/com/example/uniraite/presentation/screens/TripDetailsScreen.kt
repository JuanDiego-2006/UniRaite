package com.example.uniraite.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
// Importaciones necesarias para que lea la base de datos y encuentre el Viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.models.Viaje
import com.example.uniraite.presentation.viewmodels.ViajesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    navController: NavController,
    idViaje: Long,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState(initial = emptyList())
    val viaje = viajes.find { it.id == idViaje }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Viaje", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00965E))
            )
        }
    ) { padding ->
        if (viaje == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cargando detalles del viaje...")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AccountCircle, null, tint = Color(0xFF00965E), modifier = Modifier.size(48.dp))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(viaje.conductorNombre ?: "Conductor", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Conductor verificado", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }

                DetailItem(Icons.Filled.LocationOn, "Salida", viaje.puntoSalida)
                DetailItem(Icons.Filled.Place, "Destino", viaje.destino)
                DetailItem(Icons.Filled.AccessTime, "Horario", viaje.horaSalida)
                DetailItem(Icons.Filled.Group, "Asientos disponibles", "${viaje.asientosDisponibles}")
                DetailItem(Icons.Filled.AttachMoney, "Costo por persona", "$${viaje.costo}")

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { /* Lógica para reservar */ },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00965E)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Solicitar unirse al viaje", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color(0xFF1565C0), modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}