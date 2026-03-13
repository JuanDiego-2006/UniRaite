package com.example.uniraite.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uniraite.SesionActual
import com.example.uniraite.models.Viaje
import com.example.uniraite.presentation.viewmodels.AuthViewModel
import com.example.uniraite.presentation.viewmodels.ViajesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val GreenPrimary = Color(0xFF00965E)
private val BluePrimary = Color(0xFF1565C0)
private val BackgroundGray = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF1E1E1E)
private val TextSecondary = Color(0xFF757575)
private val DividerColor = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishTripScreen(
    onBack: () -> Unit,
    onPublishSuccess: () -> Unit,
    viajesViewModel: ViajesViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    var puntoDeSalida by remember { mutableStateOf("") }
    var destino       by remember { mutableStateOf("") }
    var fecha         by remember { mutableStateOf("") }
    var hora          by remember { mutableStateOf("") }
    var cupos         by remember { mutableStateOf("1") }
    var costo         by remember { mutableStateOf("20") }
    var descripcion   by remember { mutableStateOf("") }

    var maxCupos by remember { mutableIntStateOf(4) }

    LaunchedEffect(Unit) {
        if (SesionActual.idUsuario != 0) {
            authViewModel.verificarVehiculo(SesionActual.idUsuario) { vehiculo ->
                if (vehiculo != null) {
                    maxCupos = vehiculo.numeroAsientos
                }
            }
        }
    }

    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(context, { _, h, m ->
        val amPm = if (h >= 12) "PM" else "AM"
        val h12 = if (h % 12 == 0) 12 else h % 12
        hora = String.format("%02d:%02d %s", h12, m, amPm)
    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)

    val datePickerDialog = DatePickerDialog(context, { _, y, m, d ->
        fecha = String.format("%02d/%02d/%d", d, m + 1, y)
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Viaje", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(padding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))) {
                Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("⚡", fontSize = 14.sp)
                    Text("Comparte gastos y ayuda a otros estudiantes.",
                        fontSize = 13.sp, color = BluePrimary)
                }
            }

            TripFormField(Icons.Filled.LocationOn, GreenPrimary, "Salida", puntoDeSalida, { puntoDeSalida = it }, "Ej: Centro")
            TripFormField(Icons.Filled.LocationOn, BluePrimary, "Destino", destino, { destino = it }, "Ej: UPChiapas")

            TripFormField(Icons.Filled.DateRange, GreenPrimary, "Fecha", fecha, {}, "Toca para elegir", isReadOnly = true) { datePickerDialog.show() }
            TripFormField(Icons.Filled.AccessTime, GreenPrimary, "Hora", hora, {}, "Toca para elegir", isReadOnly = true) { timePickerDialog.show() }

            TripFormField(
                icon = Icons.Filled.Group,
                iconColor = GreenPrimary,
                label = "Cupos (Máx: $maxCupos)",
                value = cupos,
                onValueChange = { if (it.length <= 1) cupos = it },
                placeholder = "1",
                isReadOnly = false,
                keyboardType = KeyboardType.Number
            )

            TripFormField(
                icon = Icons.Filled.AttachMoney,
                iconColor = GreenPrimary,
                label = "Costo",
                value = costo,
                onValueChange = { costo = it },
                placeholder = "20",
                isReadOnly = false,
                keyboardType = KeyboardType.Number
            )

            Text("Descripción", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = descripcion, onValueChange = { descripcion = it },
                placeholder = { Text("Detalles...", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth().height(90.dp), shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = BackgroundGray)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val cuposNum = cupos.toIntOrNull() ?: 1
                    val costoNum = costo.toDoubleOrNull() ?: 0.0

                    // VALIDACIÓN DE HORA
                    var fechaValida = true
                    if (fecha.isNotBlank() && hora.isNotBlank()) {
                        try {
                            val formato = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
                            val fechaSeleccionada = formato.parse("$fecha $hora")
                            val ahora = Calendar.getInstance().time
                            if (fechaSeleccionada != null && fechaSeleccionada.before(ahora)) {
                                fechaValida = false
                            }
                        } catch (e: Exception) {
                            // Ignorar error de parseo temporalmente
                        }
                    }

                    if (puntoDeSalida.isBlank() || destino.isBlank() || fecha.isBlank() || hora.isBlank()) {
                        Toast.makeText(context, "Llena los campos obligatorios", Toast.LENGTH_SHORT).show()
                    } else if (!fechaValida) {
                        Toast.makeText(context, "No puedes publicar un viaje en el pasado", Toast.LENGTH_LONG).show()
                    } else if (SesionActual.idUsuario == 0) {
                        Toast.makeText(context, "Error: Inicia sesión de nuevo", Toast.LENGTH_LONG).show()
                    } else {
                        val viajeNube = Viaje(
                            puntoSalida = puntoDeSalida,
                            destino = if (descripcion.isNotBlank()) "$destino ($descripcion)" else destino,
                            horaSalida = "$fecha $hora",
                            costo = costoNum,
                            asientosDisponibles = cuposNum,
                            conductorId = SesionActual.idUsuario.toLong(),
                            conductorNombre = SesionActual.correoUsuario
                        )

                        viajesViewModel.publicarNuevoViaje(
                            viaje = viajeNube,
                            onSuccess = {
                                Toast.makeText(context, "¡Viaje publicado!", Toast.LENGTH_SHORT).show()
                                onPublishSuccess()
                            },
                            onError = { error ->
                                Toast.makeText(context, "Error del servidor: $error", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Publicar Viaje", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TripFormField(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isReadOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onClick: (() -> Unit)? = null
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(16.dp))
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(4.dp))
        Box(modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = isReadOnly,
                enabled = onClick == null,
                placeholder = { Text(placeholder, fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = BackgroundGray,
                    disabledTextColor = TextPrimary,
                    disabledBorderColor = DividerColor
                )
            )
        }
    }
}