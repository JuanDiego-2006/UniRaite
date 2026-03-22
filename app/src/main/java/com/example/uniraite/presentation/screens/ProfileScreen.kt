package com.example.uniraite.presentation.screens

import android.content.Context
import android.net.Uri
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
import coil.compose.SubcomposeAsyncImage
import com.example.uniraite.SesionActual
import com.example.uniraite.PreferenciasUsuario
import com.example.uniraite.models.Usuario
import com.example.uniraite.presentation.viewmodels.AuthViewModel
import com.example.uniraite.api.ApiService
import com.example.uniraite.api.RetrofitClient
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

fun guardarImagenLocalmente(context: Context, uri: Uri): String {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "perfil_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        uri.toString()
    }
}

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

    val prefsUsuario = remember { PreferenciasUsuario(context) }

    var nombre by remember { mutableStateOf(SesionActual.nombreUsuario) }
    var carrera by remember { mutableStateOf(SesionActual.carrera) }
    var fotoUri by remember { mutableStateOf(SesionActual.fotoPerfilUrl.ifEmpty { prefsUsuario.obtenerFotoUrl() }) }

    var nombreEmergencia by remember { mutableStateOf("") }
    var telefonoEmergencia by remember { mutableStateOf("") }

    var promedioEstrellas by remember { mutableDoubleStateOf(0.0) }

    // 🔥 Capturamos el rol exacto al entrar a la pantalla 🔥
    val rolActivo = remember { SesionActual.rolUsuario }

    LaunchedEffect(Unit) {
        authViewModel.obtenerUsuarioActual(SesionActual.idUsuario.toLong()) { usuario ->
            if (usuario != null) {
                nombre = usuario.nombreCompleto
                carrera = usuario.carrera ?: ""
                fotoUri = usuario.foto ?: ""
                nombreEmergencia = usuario.nombreEmergencia ?: ""
                telefonoEmergencia = usuario.telefonoEmergencia ?: ""

                SesionActual.nombreUsuario = usuario.nombreCompleto
                SesionActual.carrera = usuario.carrera ?: ""
                SesionActual.fotoPerfilUrl = usuario.foto ?: ""
                prefsUsuario.guardarFotoUrl(usuario.foto ?: "")
            }
        }

        try {
            val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
            val estrellasResponse = apiService.obtenerPromedioEstrellas(SesionActual.idUsuario.toLong())
            if (estrellasResponse.isSuccessful) {
                promedioEstrellas = estrellasResponse.body() ?: 0.0
            }
        } catch (e: Exception) { }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                fotoUri = guardarImagenLocalmente(context, uri)
            }
        }
    )

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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = telefonoEmergencia,
                        onValueChange = { telefonoEmergencia = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth()
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
                SubcomposeAsyncImage(
                    model = fotoUri.ifEmpty { null },
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(CircleShape).border(3.dp, primaryColor, CircleShape),
                    loading = {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.size(30.dp), color = primaryColor, strokeWidth = 2.dp)
                        }
                    },
                    error = {
                        Box(modifier = Modifier.fillMaxSize().background(Color.LightGray).border(3.dp, primaryColor, CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(60.dp))
                        }
                    }
                )

                if (isEditing) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(primaryColor).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 🔥 LÓGICA BLINDADA: Leemos la variable 'rolActivo' que capturamos al inicio 🔥
            if (rolActivo == "CONDUCTOR") {
                if (promedioEstrellas > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f", promedioEstrellas),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                } else {
                    Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp)) {
                        Text(
                            "Conductor Nuevo",
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(12.dp)) {
                    Text(
                        text = "Estudiante UPChiapas",
                        color = Color(0xFF1565C0),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = SesionActual.correoUsuario,
                onValueChange = { },
                label = { Text("Correo Institucional") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.DarkGray, unfocusedTextColor = Color.DarkGray)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = carrera,
                onValueChange = { carrera = it },
                label = { Text("Carrera") },
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
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
                            prefsUsuario.guardarFotoUrl(fotoUri)
                            SesionActual.fotoPerfilUrl = fotoUri
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
                        SesionActual.rolUsuario = ""
                        SesionActual.carrera = ""
                        SesionActual.fotoPerfilUrl = ""
                        prefsUsuario.limpiarPrefs()
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