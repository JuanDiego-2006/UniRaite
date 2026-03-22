package com.example.uniraite.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.presentation.viewmodels.ViajesViewModel
import com.example.uniraite.presentation.viewmodels.AuthViewModel // 🔥 Importación necesaria
import com.example.uniraite.SesionActual

@Composable
fun HomeScreen(
    navController: NavController,
    viajesViewModel: ViajesViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel() // 🔥 Agregamos el parámetro aquí
) {
    val scrollState = rememberScrollState()

    // --- COLORES DEL DISEÑO ORIGINAL (Mantenidos intactos) ---
    val primaryBlue = Color(0xFF1565C0)
    val backgroundGray = Color(0xFFF8F9FA)
    val cardBlue = Color(0xFFE3F2FD)
    val cardGreen = Color(0xFF00A669)
    val driverBannerGreen = Color(0xFFE8F5E9)
    val footerPink = Color(0xFFFFF0F6)
    val panicRed = Color(0xFFD32F2F)

    var showPanicDialog by remember { mutableStateOf(false) }
    val vehiculoUsuario by viajesViewModel.vehiculoUsuario.collectAsState()

    // --- LÓGICA DE ROLES (Mantenida intacta) ---
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                SesionActual.rolUsuario = "ESTUDIANTE"
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // --- LÓGICA DE CARGA DE DATOS + 🔥 FIREBASE INTEGRADO 🔥 ---
    LaunchedEffect(Unit) {
        // Carga de vehículo original
        viajesViewModel.cargarVehiculoPorUsuario(SesionActual.idUsuario.toLong())

        // 🔥 REGISTRO DE TOKEN (Nueva lógica invisible) 🔥
        if (SesionActual.fcmToken.isNotEmpty() && SesionActual.idUsuario > 0) {
            authViewModel.guardarTokenEnServidor(
                idUsuario = SesionActual.idUsuario.toLong(),
                token = SesionActual.fcmToken
            )
        }
    }

    // --- LÓGICA DE RESERVAS (Mantenida intacta) ---
    val misReservasRaw by viajesViewModel.misReservas.collectAsState()
    val misReservas = remember(misReservasRaw) {
        val formato = java.text.SimpleDateFormat("dd/MM/yyyy hh:mm a", java.util.Locale.US)
        val ahora = java.util.Calendar.getInstance().time

        misReservasRaw.filter { viaje ->
            try {
                val fechaViaje = formato.parse(viaje.horaSalida)
                fechaViaje?.after(ahora) == true
            } catch (e: Exception) {
                true
            }
        }
    }

    // --- DIÁLOGO DE PÁNICO (Diseño original) ---
    if (showPanicDialog) {
        AlertDialog(
            onDismissRequest = { showPanicDialog = false },
            title = { Text("¿Activar Botón de Pánico?", fontWeight = FontWeight.Bold, color = panicRed) },
            text = { Text("Se enviará tu ubicación actual y una alerta a tu contacto de emergencia y a las autoridades universitarias.") },
            confirmButton = {
                Button(onClick = { showPanicDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = panicRed)) {
                    Text("Confirmar Emergencia", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPanicDialog = false }) { Text("Cancelar", color = Color.Gray) }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // --- ESTRUCTURA DE PANTALLA (Diseño original) ---
    Scaffold(
        containerColor = backgroundGray,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPanicDialog = true },
                containerColor = panicRed,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Warning, "Botón de Pánico", modifier = Modifier.size(28.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(scrollState)
        ) {
            // --- HEADER AZUL (Diseño original) ---
            Box(
                modifier = Modifier.fillMaxWidth().background(primaryBlue).padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Hola, Estudiante 👋", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("¿A dónde vas hoy?", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                    IconButton(
                        onClick = { navController.navigate("profile") },
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.Default.Person, "Perfil", tint = Color.White)
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp).offset(y = 10.dp)) {
                // --- BANNER MODO CONDUCTOR (Diseño original) ---
                Surface(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = driverBannerGreen,
                    onClick = {
                        SesionActual.rolUsuario = "CONDUCTOR"
                        navController.navigate("driver_home")
                    }
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsCar, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("¿Vas a manejar hoy?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                            Text("Cambiar a modo conductor", fontSize = 11.sp, color = Color(0xFF2E7D32))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- CARD BUSCAR VIAJE (Diseño original) ---
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = cardGreen)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Search, null, tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Buscar Viaje", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("Encuentra viajes disponibles", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { navController.navigate("search_results") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Search, null, tint = cardGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Explorar viajes disponibles", color = cardGreen, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Tu próximo viaje")
                Spacer(modifier = Modifier.height(12.dp))

                // --- SECCIÓN PRÓXIMO VIAJE (Diseño original) ---
                if (misReservas.isNotEmpty()) {
                    val proximoViaje = misReservas.first()
                    Card(
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBlue.copy(alpha = 0.5f)),
                        border = BorderStroke(1.dp, Color(0xFFBBDEFB))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.DirectionsCar, null, tint = primaryBlue)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Conductor: ${proximoViaje.conductorNombre ?: "Registrado"}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                                            Text(" 4.9", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                                Surface(color = Color(0xFF2979FF), shape = RoundedCornerShape(4.dp)) {
                                    Text("Confirmado", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${proximoViaje.puntoSalida} → ${proximoViaje.destino}", fontSize = 13.sp, color = Color.DarkGray)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccessTime, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Hoy a las ${proximoViaje.horaSalida}", fontSize = 13.sp, color = Color.DarkGray)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate("trip_details/${proximoViaje.id ?: 0L}") },
                                modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color(0xFFBBDEFB))
                            ) {
                                Text("Ver detalles del viaje", color = Color.DarkGray, fontSize = 13.sp)
                            }
                        }
                    }
                } else {
                    // Estado vacío original
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFEEEEEE))) {
                        Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.EventNote, null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No has apartado ningún lugar", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                            Text("Los viajes que confirmes aparecerán aquí", fontSize = 13.sp, color = Color.LightGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    SectionHeader("Notificaciones")
                    Surface(color = Color(0xFFFFEBEE), shape = RoundedCornerShape(4.dp)) {
                        Text("0 nuevas", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color(0xFFD32F2F), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // (Sección de notificaciones por defecto vacía, mantenida igual)

                Spacer(modifier = Modifier.height(24.dp))
                // --- ACCIONES RÁPIDAS (Diseño original) ---
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(modifier = Modifier.weight(1f), icon = Icons.Outlined.History, iconTint = Color(0xFF9C27B0), title = "Historial", subtitle = "Tus viajes anteriores") { navController.navigate("my_reservations") }
                    QuickActionCard(modifier = Modifier.weight(1f), icon = Icons.Outlined.Person, iconTint = Color(0xFFFF9800), title = "Mi Perfil", subtitle = "Editar información") { navController.navigate("profile") }
                }

                Spacer(modifier = Modifier.height(24.dp))
                // --- FOOTER BANNER ROSA (Diseño original) ---
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = footerPink) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.School, null, tint = Color.Black, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("¿Tienes auto? ¡Conviértete en conductor!", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Comparte viajes y ahorra en gasolina", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (vehiculoUsuario != null) {
                                        SesionActual.rolUsuario = "CONDUCTOR"
                                        navController.navigate("driver_home")
                                    } else {
                                        navController.navigate("vehicle_registration")
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp), modifier = Modifier.height(32.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(6.dp), border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Text(if (vehiculoUsuario != null) "Ir al modo conductor" else "Registrar mi vehículo", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// --- COMPOSABLES AUXILIARES DE DISEÑO (Mantenidos igual) ---
@Composable
fun SectionHeader(title: String) { Text(text = title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.Black) }

@Composable
fun NotificationItem(icon: ImageVector, iconColor: Color, title: String, time: String, isNew: Boolean) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = if (isNew) Color(0xFFF1F8FF) else Color.White, border = BorderStroke(1.dp, Color(0xFFECEFF1))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(iconColor.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = if (isNew) FontWeight.Bold else FontWeight.Normal)
                Text(time, fontSize = 11.sp, color = Color.Gray)
            }
            if (isNew) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF2979FF))) }
        }
    }
}

@Composable
fun QuickActionCard(modifier: Modifier, icon: ImageVector, iconTint: Color, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = modifier.height(140.dp), shape = RoundedCornerShape(16.dp), color = Color.White, border = BorderStroke(1.dp, Color(0xFFECEFF1))) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(iconTint.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconTint)
            }
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}