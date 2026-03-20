package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.api.ReservaBackend
import com.example.uniraite.presentation.viewmodels.ViajesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationsScreen(navController: NavController, viewModel: ViajesViewModel) {
    val reservas by viewModel.historialReservas.collectAsState()
    val context = LocalContext.current

    // Variables de estado para el sistema de reseñas
    var showRatingDialog by remember { mutableStateOf(false) }
    var selectedReserva by remember { mutableStateOf<ReservaBackend?>(null) }
    var rating by remember { mutableIntStateOf(5) }
    var comentario by remember { mutableStateOf("") }

    // Cargar historial al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarHistorialReservas(SesionActual.idUsuario.toLong())
    }

    // --- DIÁLOGO DE CALIFICACIÓN (PUNTO 3) ---
    if (showRatingDialog && selectedReserva != null) {
        AlertDialog(
            onDismissRequest = { showRatingDialog = false },
            title = { Text("Calificar Viaje", fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¿Qué tal fue tu experiencia con el conductor?", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Fila de estrellitas interactivas
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Estrella $i",
                                tint = if (i <= rating) Color(0xFFFFB300) else Color.LightGray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { rating = i }
                                    .padding(4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de texto para el comentario
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        label = { Text("Escribe un comentario") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val conductorId = selectedReserva!!.viaje?.conductorId ?: 0L

                        // Creamos el objeto JSON para enviar a Spring Boot
                        val nuevaResena = com.example.uniraite.api.Resena(
                            viajeId = selectedReserva!!.viajeId,
                            evaluadorId = SesionActual.idUsuario.toLong(),
                            evaluadoId = conductorId,
                            calificacion = rating,
                            comentario = comentario
                        )

                        // Llamamos al ViewModel para enviar el POST
                        viewModel.enviarCalificacion(
                            resena = nuevaResena,
                            onSuccess = {
                                Toast.makeText(context, "¡Gracias por calificar!", Toast.LENGTH_SHORT).show()
                                showRatingDialog = false
                                comentario = ""
                                rating = 5
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                showRatingDialog = false
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Text("Enviar Reseña", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRatingDialog = false }) { Text("Cancelar", color = Color.Gray) }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Reservas", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(16.dp)
        ) {
            Text("Historial de Viajes", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1E1E))
            Spacer(modifier = Modifier.height(16.dp))

            if (reservas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes viajes reservados aún.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(reservas) { reserva ->
                        val viaje = reserva.viaje

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Etiqueta de Estado
                                    Surface(
                                        color = if (reserva.estado == "CONFIRMADA") Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = reserva.estado,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            color = if (reserva.estado == "CONFIRMADA") Color(0xFF2E7D32) else Color(0xFFE65100),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Costo
                                    if (viaje != null) {
                                        Text(text = "$${viaje.costo}0", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF00A669))
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                if (viaje != null) {
                                    // Ruta: Origen a Destino
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF1565C0), modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "${viaje.puntoSalida} → ${viaje.destino}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.DarkGray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Fecha y Hora
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = viaje.horaSalida, fontSize = 14.sp, color = Color.Gray)
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // 🔥 BOTÓN DEL PUNTO 3: CALIFICAR VIAJE
                                    Button(
                                        onClick = {
                                            selectedReserva = reserva
                                            rating = 5 // Reseteamos a 5 estrellas
                                            comentario = ""
                                            showRatingDialog = true
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF8E1)),
                                        border = BorderStroke(1.dp, Color(0xFFFFECB3)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Calificar Conductor", color = Color(0xFFF57F17), fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Text("Detalles del viaje no disponibles", color = Color.Gray, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}