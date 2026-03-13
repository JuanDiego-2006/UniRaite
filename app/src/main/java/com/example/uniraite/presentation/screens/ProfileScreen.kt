package com.example.uniraite.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.uniraite.SesionActual
import com.example.uniraite.presentation.viewmodels.AuthViewModel
import com.example.uniraite.models.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val primaryColor = if (SesionActual.rolUsuario == "CONDUCTOR") Color(0xFF2E7D32) else Color(0xFF1565C0)
    val backgroundGray = Color(0xFFF8F9FA)

    // 1. ESTADO DEL USUARIO
    var usuarioActual by remember { mutableStateOf<Usuario?>(null) }
    var mostrarPopupInfo by remember { mutableStateOf(false) }
    var mostrarPopupEmergencia by remember { mutableStateOf(false) }

    // 2. AQUÍ ESTÁN LAS VARIABLES QUE FALTABAN (Esto corrige el Unresolved reference)
    var nombreEmergencia by remember { mutableStateOf("Sin configurar") }
    var telefonoEmergencia by remember { mutableStateOf("Sin configurar") }

    // Variables temporales para cuando el usuario está escribiendo en el popup
    var tempNombreEmergencia by remember { mutableStateOf("") }
    var tempTelefonoEmergencia by remember { mutableStateOf("") }

    // Estado para la foto de perfil
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    // 3. CARGAR DATOS DESDE AWS
    LaunchedEffect(Unit) {
        val idLogueado = SesionActual.idUsuario.toLong()
        authViewModel.obtenerUsuarioActual(idLogueado) { usuario ->
            usuario?.let {
                usuarioActual = it
                // Sincronizamos las variables con la base de datos de AWS
                nombreEmergencia = it.nombreEmergencia ?: "Sin configurar"
                telefonoEmergencia = it.telefonoEmergencia ?: "Sin configurar"
            }
        }
    }

    Scaffold(
        containerColor = backgroundGray,
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
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

            // FOTO DE PERFIL
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.1f))
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                val imagenAMostrar = selectedImageUri ?: usuarioActual?.foto

                if (imagenAMostrar != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imagenAMostrar)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = "Añadir foto",
                        modifier = Modifier.size(40.dp),
                        tint = primaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = usuarioActual?.nombreCompleto ?: "Cargando...", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = usuarioActual?.carrera ?: "UPChiapas", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Botón Información Personal
                Button(
                    onClick = { mostrarPopupInfo = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Info, null, tint = primaryColor)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Información Personal", color = Color.Black, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
                    }
                }

                // Botón Contacto de Emergencia
                Button(
                    onClick = {
                        tempNombreEmergencia = if (nombreEmergencia == "Sin configurar") "" else nombreEmergencia
                        tempTelefonoEmergencia = if (telefonoEmergencia == "Sin configurar") "" else telefonoEmergencia
                        mostrarPopupEmergencia = true
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.ContactPhone, null, tint = primaryColor)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Contacto de Emergencia", color = Color.Black, fontSize = 14.sp)
                            Text(text = nombreEmergencia, color = Color.Gray, fontSize = 12.sp)
                        }
                        Icon(Icons.Default.Edit, null, tint = Color.Gray)
                    }
                }

                // Botón Mi Vehículo (Solo si es conductor)
                if (SesionActual.rolUsuario == "CONDUCTOR") {
                    Button(
                        onClick = { navController.navigate("edit_vehicle") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.DirectionsCar, null, tint = primaryColor)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Mi Vehículo", color = Color.Black, modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón Cerrar Sesión
                Button(
                    onClick = {
                        SesionActual.correoUsuario = ""
                        SesionActual.rolUsuario = ""
                        navController.navigate("login") { popUpTo(0) }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ExitToApp, null, tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // DIÁLOGO INFORMACIÓN PERSONAL
        if (mostrarPopupInfo && usuarioActual != null) {
            AlertDialog(
                onDismissRequest = { mostrarPopupInfo = false },
                title = { Text("Mis Datos") },
                text = {
                    Column {
                        Text("Nombre: ${usuarioActual!!.nombreCompleto}")
                        Text("Matrícula: ${usuarioActual!!.matricula}")
                        Text("Correo: ${usuarioActual!!.correoInstitucional}")
                        Text("Teléfono: ${usuarioActual!!.telefono}")
                    }
                },
                confirmButton = { TextButton(onClick = { mostrarPopupInfo = false }) { Text("OK") } }
            )
        }

        // DIÁLOGO CONTACTO EMERGENCIA
        if (mostrarPopupEmergencia) {
            AlertDialog(
                onDismissRequest = { mostrarPopupEmergencia = false },
                title = { Text("Configurar Emergencia") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = tempNombreEmergencia,
                            onValueChange = { tempNombreEmergencia = it },
                            label = { Text("Nombre del contacto") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = tempTelefonoEmergencia,
                            onValueChange = { tempTelefonoEmergencia = it },
                            label = { Text("Teléfono") },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            authViewModel.guardarContactoEmergencia(
                                SesionActual.idUsuario,
                                tempNombreEmergencia,
                                tempTelefonoEmergencia
                            ) {
                                // Al guardar con éxito, actualizamos la pantalla
                                nombreEmergencia = tempNombreEmergencia
                                telefonoEmergencia = tempTelefonoEmergencia
                                mostrarPopupEmergencia = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) { Text("Guardar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarPopupEmergencia = false }) { Text("Cancelar") }
                }
            )
        }
    }
}