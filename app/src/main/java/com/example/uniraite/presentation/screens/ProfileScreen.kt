package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.uniraite.SesionActual
import com.example.uniraite.models.Usuario
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val primaryColor = Color(0xFF1565C0)
    val redColor = Color(0xFFD32F2F)

    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showEmergencyDialog by remember { mutableStateOf(false) }

    // Estados del perfil
    var nombre by remember { mutableStateOf(SesionActual.nombreUsuario) }
    var carrera by remember { mutableStateOf(SesionActual.carrera) }
    var fotoUri by remember { mutableStateOf(SesionActual.fotoPerfilUrl) }

    // Estados del contacto de emergencia
    var nombreEmergencia by remember { mutableStateOf("") }
    var telefonoEmergencia by remember { mutableStateOf("") }

    // Al abrir la pantalla, jalamos la información desde la base de datos remota
    LaunchedEffect(Unit) {
        authViewModel.obtenerUsuarioActual(SesionActual.idUsuario.toLong()) { usuario ->
            if (usuario != null) {
                nombre = usuario.nombreCompleto
                carrera = usuario.carrera ?: ""
                fotoUri = usuario.foto ?: ""
                nombreEmergencia = usuario.nombreEmergencia ?: ""
                telefonoEmergencia = usuario.telefonoEmergencia ?: ""

                // Actualizamos sesión local por si acaso
                SesionActual.nombreUsuario = usuario.nombreCompleto
                SesionActual.carrera = usuario.carrera ?: ""
                SesionActual.fotoPerfilUrl = usuario.foto ?: ""
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) fotoUri = uri.toString() }
    )

    // Diálogo de Contacto de Emergencia
    if (showEmergencyDialog) {
        AlertDialog(
            onDismissRequest = { showEmergencyDialog = false },
            title = { Text("Contacto de Emergencia", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Esta persona será contactada en caso de un incidente.", color = Color.Gray, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nombreEmergencia,
                        onValueChange = { nombreEmergencia = it },
                        label = { Text("Nombre Completo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = telefonoEmergencia,
                        onValueChange = { telefonoEmergencia = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    authViewModel.guardarContactoEmergencia(
                        idUsuario = SesionActual.idUsuario.toLong(),
                        nombre = nombreEmergencia,
                        telefono = telefonoEmergencia,
                        onSuccess = {
                            Toast.makeText(context, "Contacto de emergencia guardado", Toast.LENGTH_SHORT).show()
                            showEmergencyDialog = false
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        }
                    )
                }, colors = ButtonDefaults.buttonColors(containerColor = primaryColor)) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = { TextButton(onClick = { showEmergencyDialog = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = Color.White)
                    }
                },
                actions = {
                    // Botón de Contacto de Emergencia
                    IconButton(onClick = { showEmergencyDialog = true }) {
                        Icon(Icons.Default.Warning, "Contacto de Emergencia", tint = Color.White)
                    }
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, "Editar", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(120.dp).clickable(enabled = isEditing) {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                contentAlignment = Alignment.BottomEnd
            ) {
                if (fotoUri.isNotEmpty()) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(CircleShape).border(3.dp, primaryColor, CircleShape)
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color.LightGray).border(3.dp, primaryColor, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(60.dp))
                    }
                }
                if (isEditing) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(primaryColor).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Se cambia enabled = isEditing por readOnly = !isEditing para que el color no se ponga gris/invisible
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = SesionActual.correoUsuario,
                onValueChange = { },
                label = { Text("Correo Institucional") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = carrera,
                onValueChange = { carrera = it },
                label = { Text("Carrera") },
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isEditing) {
                Button(
                    onClick = {
                        isLoading = true
                        val usuarioEditado = Usuario(
                            id = SesionActual.idUsuario.toLong(),
                            nombreCompleto = nombre,
                            correoInstitucional = SesionActual.correoUsuario,
                            carrera = carrera,
                            foto = fotoUri
                        )
                        authViewModel.actualizarPerfil(usuarioEditado, {
                            isLoading = false
                            isEditing = false
                            Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                        }, {
                            isLoading = false
                            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
                        })
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Guardar Cambios", fontWeight = FontWeight.Bold)
                }
            } else {
                OutlinedButton(
                    onClick = {
                        SesionActual.idUsuario = 0
                        SesionActual.nombreUsuario = ""
                        SesionActual.correoUsuario = ""
                        SesionActual.carrera = ""
                        SesionActual.fotoPerfilUrl = ""
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = redColor)
                ) {
                    Icon(Icons.Default.ExitToApp, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}