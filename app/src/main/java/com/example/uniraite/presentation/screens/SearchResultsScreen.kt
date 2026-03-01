package com.example.uniraite.presentation.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ViajeDisponible(
    val id: String,
    val conductorNombre: String,
    val conductorIniciales: String,
    val rating: Float,
    val origen: String,
    val destino: String,
    val hora: String,
    val lugares: Int,
    val vehiculo: String,
    val precio: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(navController: NavController) {
    var zonaSearch by remember { mutableStateOf("") }
    
    val primaryBlue = Color(0xFF1565C0)
    val backgroundGray = Color(0xFFF8F9FA)
    val cardGreen = Color(0xFF00A669)

    val viajes = listOf(
        ViajeDisponible("1", "María González", "MG", 4.9f, "Col. Centro",   "UPChiapas",    "07:00 AM", 3, "Toyota Corolla 2020", 30),
        ViajeDisponible("2", "Carlos Méndez",  "CM", 4.7f, "Plaza Crystal", "UPChiapas",    "07:30 AM", 2, "Honda Civic 2019",    25),
        ViajeDisponible("3", "Ana Martínez",   "AM", 4.8f, "UPChiapas",     "Col. Centro",  "02:00 PM", 4, "Nissan Versa 2021",   20),
        ViajeDisponible("4", "Luis Torres",    "LT", 4.6f, "UPChiapas",     "Plaza Crystal","06:00 PM", 1, "Mazda 3 2022",        35),
    )

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
                Surface(
                    shadowElevation = 4.dp, 
                    color = primaryBlue
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = zonaSearch,
                            onValueChange = { zonaSearch = it },
                            placeholder = { Text("¿A dónde vas?", fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Resultados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Surface(
                    color = cardGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "${viajes.size} rumbos",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = cardGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viajes) { viaje ->
                    ViajeItem(viaje = viaje, primaryBlue = primaryBlue, cardGreen = cardGreen)
                }
                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun ViajeItem(viaje: ViajeDisponible, primaryBlue: Color, cardGreen: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(primaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(viaje.conductorIniciales, color = primaryBlue, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(viaje.conductorNombre, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                        Text(" ${viaje.rating}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Text(
                    "\$${viaje.precio}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = cardGreen
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("${viaje.origen} → ${viaje.destino}", fontSize = 14.sp, color = Color.DarkGray)
            }
            
            Spacer(Modifier.height(8.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(viaje.hora, fontSize = 13.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${viaje.lugares} disponibles", fontSize = 13.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Navegar a detalles */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
            ) {
                Text("Solicitar Raite", fontWeight = FontWeight.Bold)
            }
        }
    }
}
