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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniraite.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // Traemos el ViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val PrimaryGreen = Color(0xFF006400)
    val BackgroundColor = Color(0xFFF0F2F5)

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundColor).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("UniRaite", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Text("Viaja seguro a tu universidad", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Correo Institucional") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // BOTÓN: OLVIDÉ MI CONTRASEÑA
        Text(
            text = "¿Olvidaste tu contraseña?",
            color = PrimaryGreen,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.End).clickable { navController.navigate("forgot_password") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    // LLAMAMOS A LA BASE DE DATOS PARA VALIDAR
                    authViewModel.loginUsuario(email, password) { loginExitoso ->
                        if (loginExitoso) {
                            Toast.makeText(context, "¡Bienvenido a UniRaite!", Toast.LENGTH_SHORT).show()
                            // Si entra, lo mandamos al Home y borramos el Login del historial
                            navController.navigate("home") { popUpTo("login") { inclusive = true } }
                        } else {
                            Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Ingresa tus datos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Iniciar Sesión", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("¿No tienes cuenta? ")
            Text("Regístrate", color = PrimaryGreen, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("register") })
        }
    }
}