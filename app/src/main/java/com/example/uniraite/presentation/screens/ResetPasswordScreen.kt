package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    email: String,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var nuevaContrasena by remember { mutableStateOf("") }
    val PrimaryGreen = Color(0xFF006400)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Nueva Contraseña", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cuenta: $email", color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nuevaContrasena,
            onValueChange = { nuevaContrasena = it },
            label = { Text("Escribe tu nueva contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nuevaContrasena.length >= 4) {
                    // AQUÍ ESTÁ LA CORRECCIÓN: Se agregan onSuccess y onError
                    authViewModel.actualizarContrasena(
                        correo = email,
                        nuevaContrasena = nuevaContrasena,
                        onSuccess = {
                            Toast.makeText(context, "¡Contraseña actualizada con éxito!", Toast.LENGTH_LONG).show()
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onError = {
                            Toast.makeText(context, "Error al actualizar. Intenta de nuevo.", Toast.LENGTH_LONG).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Guardar y Entrar", fontSize = 18.sp, color = Color.White)
        }
    }
}