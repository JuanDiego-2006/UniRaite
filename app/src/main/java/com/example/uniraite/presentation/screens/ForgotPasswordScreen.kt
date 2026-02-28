package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val PrimaryGreen = Color(0xFF006400)

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Text("Recuperar Contraseña", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ingresa tu correo institucional para buscar tu cuenta.", fontSize = 16.sp, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Correo Institucional") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.contains("@")) {
                    // Validamos en la Base de Datos
                    authViewModel.verificarCorreo(email) { existe ->
                        if (existe) {
                            // Si existe, lo mandamos a crear la nueva contraseña y pasamos el correo en la ruta
                            navController.navigate("reset_password/$email")
                        } else {
                            Toast.makeText(context, "Este correo no está registrado", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Buscar Cuenta", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Regresar al Login", color = PrimaryGreen, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.popBackStack() })
    }
}