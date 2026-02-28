package com.example.uniraite.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // Traemos el ViewModel aquí
) {
    val context = LocalContext.current // Para mostrar mensajes (Toasts)

    var name by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val PrimaryGreen = Color(0xFF006400)
    val BackgroundColor = Color(0xFFF0F2F5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = matricula, onValueChange = { matricula = it },
            label = { Text("Matrícula") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Correo Institucional") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = telefono, onValueChange = { telefono = it },
            label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if(name.isNotEmpty() && matricula.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    // LLAMAMOS A LA BASE DE DATOS
                    authViewModel.registrarUsuario(name, matricula, email, telefono, password) {
                        Toast.makeText(context, "¡Registro Exitoso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") // Si sale bien, lo mandamos al login
                    }
                } else {
                    Toast.makeText(context, "Llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Registrarse", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("¿Ya tienes cuenta? ")
            Text("Inicia Sesión", color = PrimaryGreen, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("login") })
        }
    }
}