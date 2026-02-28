package com.example.uniraite.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val primaryBlue = Color(0xFF1565C0)
    val backgroundGray = Color(0xFFF8F9FA)
    val cardBlue = Color(0xFFE3F2FD)
    val cardGreen = Color(0xFF00A669)
    val driverBannerGreen = Color(0xFFE8F5E9)
    val notificationBlue = Color(0xFFF0F7FF)
    val footerPink = Color(0xFFFFF0F6)

    Scaffold(
        containerColor = backgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Header Azul
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primaryBlue)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hola, Estudiante 👋",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "¿A dónde vas hoy?",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate("profile") },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = 10.dp) // Cambiado de -16.dp a 10.dp para bajar el cuadro
            ) {
                // Banner Modo Conductor
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = driverBannerGreen
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "¿Vas a manejar hoy?",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                "Cambiar a modo conductor",
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card Buscar Viaje
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardGreen)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Buscar Viaje", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("Encuentra viajes disponibles", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { navController.navigate("search") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = cardGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Explorar viajes disponibles", color = cardGreen, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Próximo Viaje
                SectionHeader("Tu próximo viaje")
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBlue.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, Color(0xFFBBDEFB))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = primaryBlue)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Maria González", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                                        Text(" 4.9", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                            }
                            Surface(
                                color = Color(0xFF2979FF),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    "Confirmado",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Col. Centro → UPChiapas", fontSize = 13.sp, color = Color.DarkGray)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Mañana a las 07:00 AM", fontSize = 13.sp, color = Color.DarkGray)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFBBDEFB))
                        ) {
                            Text("Ver detalles del viaje", color = Color.DarkGray, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Notificaciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("Notificaciones")
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "2 nuevas",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color(0xFFD32F2F),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NotificationItem(
                        icon = Icons.Outlined.Notifications,
                        iconColor = Color(0xFF4CAF50),
                        title = "Tu viaje con Maria González está confirmado",
                        time = "Hace 5 min",
                        isNew = true
                    )
                    NotificationItem(
                        icon = Icons.Default.NotificationsActive,
                        iconColor = Color(0xFFFFB300),
                        title = "Recuerda: Viaje mañana a las 7:00 AM",
                        time = "Hace 1 hora",
                        isNew = true
                    )
                    NotificationItem(
                        icon = Icons.Outlined.Notifications,
                        iconColor = Color(0xFF2979FF),
                        title = "Carlos te ha calificado con 5 estrellas",
                        time = "Hace 2 horas",
                        isNew = false
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Historial y Perfil
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.History,
                        iconTint = Color(0xFF9C27B0),
                        title = "Historial",
                        subtitle = "Tus viajes anteriores",
                        onClick = { }
                    )
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Person,
                        iconTint = Color(0xFFFF9800),
                        title = "Mi Perfil",
                        subtitle = "Editar información",
                        onClick = { navController.navigate("profile") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Footer Banner Conductor
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = footerPink
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "¿Tienes auto? ¡Conviértete en conductor!",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Comparte viajes y ahorra en gasolina",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Text("Registrar mi vehículo", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun NotificationItem(icon: ImageVector, iconColor: Color, title: String, time: String, isNew: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isNew) Color(0xFFF1F8FF) else Color.White,
        border = BorderStroke(1.dp, Color(0xFFECEFF1))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = if (isNew) FontWeight.Bold else FontWeight.Normal)
                Text(time, fontSize = 11.sp, color = Color.Gray)
            }
            if (isNew) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2979FF))
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(modifier: Modifier, icon: ImageVector, iconTint: Color, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFECEFF1))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}
