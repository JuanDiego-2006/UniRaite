package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.SesionActual
import com.example.uniraite.models.Viaje
import com.example.uniraite.presentation.viewmodels.AuthViewModel
import com.example.uniraite.presentation.viewmodels.ViajesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverHomeScreen(
    navController: NavController,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val primaryGreen = Color(0xFF2E7D32)
    val backgroundGray = Color(0xFFF8F9FA)
    val context = LocalContext.current

    val misViajes by viajesViewModel.misViajes.collectAsState()

    LaunchedEffect(Unit) {
        if (SesionActual.idUsuario != 0) {
            viajesViewModel.cargarViajesPorConductor(SesionActual.idUsuario.toLong())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Viajes Publicados", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = primaryGreen)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("publish_trip") },
                containerColor = primaryGreen,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Publicar Viaje") }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, null) },
                    label = { Text("Mi Perfil") },
                    selected = false,
                    onClick = { navController.navigate("profile") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGray)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (misViajes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.DirectionsCar, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                        Spacer(Modifier.height(10.dp))
                        Text("Aún no has publicado rumbos", color = Color.Gray, fontSize = 16.sp)
                    }
                }
            } else {
                Text(
                    "Activos actualmente:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(misViajes) { viaje ->
                        CardViajeConductor(viaje, viajesViewModel, context)
                    }
                }
            }
        }
    }
}

@Composable
fun CardViajeConductor(viaje: Viaje, viewModel: ViajesViewModel, context: android.content.Context) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var editHora by remember { mutableStateOf(viaje.horaSalida) }
    var editAsientos by remember { mutableStateOf(viaje.asientosDisponibles.toString()) }

    val authViewModel: AuthViewModel = viewModel()
    var maxAsientosVehiculo by remember { mutableIntStateOf(4) }

    LaunchedEffect(showEditDialog) {
        if (showEditDialog) {
            authViewModel.verificarVehiculo(SesionActual.idUsuario) { vehiculo ->
                if (vehiculo != null) maxAsientosVehiculo = vehiculo.numeroAsientos
            }
        }
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val timePickerDialog = android.app.TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth, hourOfDay, minute)
                    val format = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
                    editHora = format.format(selectedCalendar.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Cancelar Viaje", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F)) },
            text = { Text("¿Estás seguro de que deseas cancelar este viaje?") },
            confirmButton = {
                Button(
                    onClick = {
                        val idViaje = viaje.id ?: 0L
                        viewModel.eliminarViaje(idViaje, SesionActual.idUsuario.toLong()) {
                            Toast.makeText(context, "Viaje cancelado", Toast.LENGTH_SHORT).show()
                            showDeleteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Atrás") }
            }
        )
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Viaje", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Toca la casilla para elegir fecha y hora:", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() }
                    ) {
                        OutlinedTextField(
                            value = editHora,
                            onValueChange = { },
                            label = { Text("Fecha y Hora") },
                            enabled = false,
                            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF2E7D32)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = Color.Black,
                                disabledBorderColor = Color.Gray,
                                disabledLabelColor = Color.DarkGray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = editAsientos,
                        onValueChange = { editAsientos = it },
                        label = { Text("Asientos Disponibles (Máx: $maxAsientosVehiculo)") },
                        leadingIcon = { Icon(Icons.Default.Group, contentDescription = null, tint = Color(0xFF2E7D32)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val asientosInt = editAsientos.toIntOrNull() ?: viaje.asientosDisponibles
                        var esFechaValida = true
                        try {
                            val formato = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
                            val fechaSeleccionada = formato.parse(editHora)
                            val ahora = Calendar.getInstance().time
                            if (fechaSeleccionada != null && fechaSeleccionada.before(ahora)) {
                                esFechaValida = false
                            }
                        } catch (e: Exception) { esFechaValida = false }

                        if (!esFechaValida) {
                            Toast.makeText(context, "No puedes programar un viaje al pasado", Toast.LENGTH_SHORT).show()
                        } else if (asientosInt > maxAsientosVehiculo) {
                            Toast.makeText(context, "Tu vehículo solo tiene $maxAsientosVehiculo asientos", Toast.LENGTH_SHORT).show()
                        } else if (asientosInt <= 0) {
                            Toast.makeText(context, "Debe haber al menos 1 asiento", Toast.LENGTH_SHORT).show()
                        } else {
                            val viajeActualizado = viaje.copy(horaSalida = editHora, asientosDisponibles = asientosInt)
                            val idViaje = viaje.id ?: 0L

                            viewModel.editarViaje(idViaje, viajeActualizado) {
                                Toast.makeText(context, "Viaje actualizado", Toast.LENGTH_SHORT).show()
                                showEditDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Destino: ${viaje.destino}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF2E7D32),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                Text(text = "$${viaje.costo}", fontWeight = FontWeight.ExtraBold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = "De: ${viaje.puntoSalida}", fontSize = 14.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = "Hora: ${viaje.horaSalida}", fontSize = 14.sp, color = Color.Gray)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Cupos: ${viaje.asientosDisponibles}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                Row {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F))
                    }
                }
            }
        }
    }
}