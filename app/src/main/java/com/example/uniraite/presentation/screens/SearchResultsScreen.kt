package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.data.local.entities.Viaje
import com.example.uniraite.presentation.viewmodels.ViajesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    var zonaSearch by remember { mutableStateOf("") }
    val primaryBlue = Color(0xFF1565C0)
    val backgroundGray = Color(0xFFF8F9FA)
    val cardGreen = Color(0xFF00A669)

    val viajesReales by viajesViewModel.viajes.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viajesViewModel.cargarViajesReales()
    }

    Scaffold(
        containerColor = backgroundGray,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Buscar Viajes", fontWeight = FontWeight.Bold, color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryBlue)
                )
                Surface(shadowElevation = 4.dp, color = primaryBlue) {
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                        OutlinedTextField(
                            value = zonaSearch, onValueChange = { zonaSearch = it },
                            placeholder = { Text("¿A dónde vas?", fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp), singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Resultados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Surface(color = cardGreen.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        "${viajesReales.size} rumbos", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = cardGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (viajesReales.isEmpty()) {
                    item { Text("Aún no hay viajes publicados", color = Color.Gray, modifier = Modifier.padding(16.dp)) }
                } else {
                    items(viajesReales) { viaje ->
                        ViajeItemReal(
                            viaje = viaje, 
                            primaryBlue = primaryBlue, 
                            cardGreen = cardGreen,
                            onReservar = {
                                viajesViewModel.apartarLugar(viaje.idViaje, SesionActual.idUsuario) {
                                    Toast.makeText(context, "¡Lugar reservado con éxito!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack() // Volver al Home para ver el viaje reservado
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ViajeItemReal(viaje: Viaje, primaryBlue: Color, cardGreen: Color, onReservar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(primaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("C${viaje.idConductor}", color = primaryBlue, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Conductor #${viaje.idConductor}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                        Text(" Nuevo", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Text("\$${viaje.costoPorPersona}0", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = cardGreen)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("${viaje.puntoSalida} → ${viaje.destino}", fontSize = 14.sp, color = Color.DarkGray)
            }

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(viaje.horaSalida, fontSize = 13.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${viaje.cuposDisponibles} disponibles", fontSize = 13.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onReservar, 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp), 
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
            ) {
                Text("Apartar Lugar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
