package com.example.uniraite.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.presentation.viewmodels.AuthViewModel
import com.example.uniraite.data.local.entities.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val primaryBlue = Color(0xFF1565C0)
    val backgroundGray = Color(0xFFF8F9FA)

    var usuarioActual by remember { mutableStateOf<Usuario?>(null) }
    var mostrarPopupInfo by remember { mutableStateOf(false) }
    var mostrarPopupEmergencia by remember { mutableStateOf(false) }

    // Estados para el contacto de emergencia
    var nombreEmergencia by remember { mutableStateOf("Mamá") }
    var telefonoEmergencia by remember { mutableStateOf("961 123 4567") }
    
    // Estados temporales para el diálogo
    var tempNombreEmergencia by remember { mutableStateOf(nombreEmergencia) }
    var tempTelefonoEmergencia by remember { mutableStateOf(telefonoEmergencia) }

    // Cargar datos del usuario
    LaunchedEffect(Unit) {
        authViewModel.obtenerUsuarioActual(SesionActual.correoUsuario) { usuarioEncontrado ->
            usuarioActual = usuarioEncontrado
        }
    }

    Scaffold(
        containerColor = backgroundGray,
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryBlue)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Foto genérica
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(primaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = primaryBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = usuarioActual?.nombreCompleto ?: "Cargando...",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "UPChiapas",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // BOTONES DE CONFIGURACIÓN
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Información Personal
                Button(
                    onClick = { mostrarPopupInfo = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = primaryBlue)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Información Personal", color = Color.Black, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
                    }
                }

                // Contacto de Emergencia
                Button(
                    onClick = { 
                        tempNombreEmergencia = nombreEmergencia
                        tempTelefonoEmergencia = telefonoEmergencia
                        mostrarPopupEmergencia = true 
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ContactPhone, contentDescription = null, tint = primaryBlue)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Configurar contacto de emergencia", color = Color.Black, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cerrar Sesión
                Button(
                    onClick = {
                        SesionActual.correoUsuario = ""
                        navController.navigate("login") { popUpTo(0) }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ExitToApp, contentDescription = null, tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        // VENTANA INFORMACIÓN PERSONAL
        if (mostrarPopupInfo && usuarioActual != null) {
            AlertDialog(
                onDismissRequest = { mostrarPopupInfo = false },
                title = { Text("Mis Datos", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Nombre: ${usuarioActual!!.nombreCompleto}", fontSize = 16.sp)
                        Text("Matrícula: ${usuarioActual!!.matricula}", fontSize = 16.sp)
                        Text("Correo: ${usuarioActual!!.correoInstitucional}", fontSize = 16.sp)
                        Text("Teléfono: ${usuarioActual!!.telefono}", fontSize = 16.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { mostrarPopupInfo = false }) {
                        Text("Cerrar", fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White
            )
        }

        // VENTANA CONTACTO EMERGENCIA (EDITABLE)
        if (mostrarPopupEmergencia) {
            AlertDialog(
                onDismissRequest = { mostrarPopupEmergencia = false },
                title = { Text("Contacto de Emergencia", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = tempNombreEmergencia,
                            onValueChange = { tempNombreEmergencia = it },
                            label = { Text("Nombre del contacto") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = tempTelefonoEmergencia,
                            onValueChange = { tempTelefonoEmergencia = it },
                            label = { Text("Teléfono") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            nombreEmergencia = tempNombreEmergencia
                            telefonoEmergencia = tempTelefonoEmergencia
                            mostrarPopupEmergencia = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarPopupEmergencia = false }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                containerColor = Color.White
            )
        }
    }
}
