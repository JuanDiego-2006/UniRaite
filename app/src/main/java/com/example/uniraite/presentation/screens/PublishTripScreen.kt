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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishTripScreen(
    onTripPublished: () -> Unit, // Acción al terminar
    onBack: () -> Unit // Acción para volver atrás
) {
    // Variables para el formulario
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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Detalles de la Ruta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Origen
            OutlinedTextField(
                value = origen,
                onValueChange = { origen = it },
                label = { Text("Punto de Partida (Origen)") },
                placeholder = { Text("Ej. Parque de la Marimba") },
                leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Destino
            OutlinedTextField(
                value = destino,
                onValueChange = { destino = it },
                label = { Text("Destino") },
                leadingIcon = { Icon(Icons.Filled.DirectionsCar, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Hora
            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora de Salida") },
                placeholder = { Text("Ej. 07:00 AM") },
                leadingIcon = { Icon(Icons.Filled.Schedule, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Cupos
                OutlinedTextField(
                    value = cupos,
                    onValueChange = { cupos = it },
                    label = { Text("Cupos") },
                    placeholder = { Text("4") },
                    leadingIcon = { Icon(Icons.Filled.Group, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                // Precio
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Costo") },
                    placeholder = { Text("$20") },
                    leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Publicar
            Button(
                onClick = {
                    if (origen.isBlank() || hora.isBlank() || cupos.isBlank()) {
                        Toast.makeText(context, "Por favor completa los datos", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "¡Viaje Publicado Exitosamente!", Toast.LENGTH_SHORT).show()
                        onTripPublished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Publicar Viaje", fontSize = 18.sp)
            }
        }
    }
}