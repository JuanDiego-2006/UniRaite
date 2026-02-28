package com.example.uniraite.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Si ya tienes esta clase en otro archivo, bórrala de aquí. Si no, déjala.
data class SearchResult(
    val name: String,
    val route: String,
    val price: String,
    val rating: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(navController: NavController) { // <--- ESTA ES LA CLAVE
    val results = listOf(
        SearchResult("Juan Pérez", "Tuxtla -> UPChiapas", "$25", 4.8),
        SearchResult("Ana García", "Centro -> UPChiapas", "$30", 4.9)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF006400))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(results) { trip ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.LightGray))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(trip.name, fontWeight = FontWeight.Bold)
                                Text(trip.route, color = Color.Gray)
                                Row {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                                    Text(" ${trip.rating}", fontSize = 12.sp)
                                }
                            }
                            Text(trip.price, fontWeight = FontWeight.Bold, color = Color(0xFF006400), fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}