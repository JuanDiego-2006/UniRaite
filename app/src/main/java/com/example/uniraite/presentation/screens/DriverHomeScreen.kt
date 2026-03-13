package com.example.uniraite.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uniraite.presentation.viewmodels.ViajesViewModel
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.models.Viaje

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverHomeScreen(
    navController: NavController,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val primaryGreen = Color(0xFF2E7D32)
    val backgroundGray = Color(0xFFF8F9FA)

    // CORRECCIÓN: Observamos la variable de estado segura
    val misViajes by viajesViewModel.misViajes.collectAsState()

    // CORRECCIÓN: Llamamos a la API una sola vez cuando se abre la pantalla
    LaunchedEffect(Unit) {
        if (SesionActual.idUsuario != 0) {
            viajesViewModel.cargarViajesPorConductor(SesionActual.idUsuario.toLong())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Viajes Publicados", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = primaryGreen)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("publish_trip") }, // Corrección de la ruta a publish_trip
                containerColor = primaryGreen,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Publicar Viaje") }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, null) },
                    label = { Text("Mi Perfil") },
                    selected = false,
                    onClick = { navController.navigate("profile") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGray)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (misViajes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.DirectionsCar, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                        Spacer(Modifier.height(10.dp))
                        Text("Aún no has publicado rumbos", color = Color.Gray, fontSize = 16.sp)
                    }
                }
            } else {
                Text(
                    "Activos actualmente:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(misViajes) { viaje ->
                        CardViajeConductor(viaje)
                    }
                }
            }
        }
    }
}

@Composable
fun CardViajeConductor(viaje: Viaje) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Destino: ${viaje.destino}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2E7D32))
                Text(text = "$${viaje.costo}0", fontWeight = FontWeight.ExtraBold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = "De: ${viaje.puntoSalida}", fontSize = 14.sp, color = Color.Gray)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = "Hora: ${viaje.horaSalida}", fontSize = 14.sp, color = Color.Gray)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 0.5.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Group, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = "Cupos: ${viaje.asientosDisponibles}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}