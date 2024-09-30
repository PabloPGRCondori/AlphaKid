package com.example.alphakid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alphakid.ui.theme.AlphaKidTheme // Update to use AlphaKidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlphaKidTheme {  // Change to AlphaKidTheme
                val navController = rememberNavController()
                AppNavigator(navController)
            }
        }
    }
}
@Composable
fun InitialScreen(onSettingsClick: () -> Unit, onLoginClick: () -> Unit) {
    AlphaKidTheme {  // Applying your custom AlphaKid theme
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title "AlphaKid"
            Text(
                text = "AlphaKid",
                style = MaterialTheme.typography.headlineMedium,  // Using the typography from your theme
                color = MaterialTheme.colorScheme.primary  // Use the primary color
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Central logo
            Icon(
                painter = painterResource(id = R.drawable.logo),  // Replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp),  // Set the logo size
                tint = MaterialTheme.colorScheme.primary  // Tint according to theme
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Button for Settings
            Button(
                onClick = onSettingsClick,
                modifier = Modifier.fillMaxWidth(0.8f),  // 80% of the screen width
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = "Configuración", color = MaterialTheme.colorScheme.onSecondary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button for optional Login
            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = "Iniciar Sesión", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
@Composable
fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            InitialScreen(
                onSettingsClick = { navController.navigate("settings_screen") },
                onLoginClick = { navController.navigate("login_screen") }
            )
        }
        composable("settings_screen") {
            SettingsScreen()
        }
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("notifications_screen") {
            NotificationsScreen()
        }
    }
}

// Pantalla de configuración
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla de Configuración", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Agregar opciones de configuración aquí
        Text(text = "Opción 1: Configuración del dispositivo")
        Text(text = "Opción 2: Preferencias")
    }
}

// Pantalla de inicio de sesión
@Composable
fun LoginScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Inputs para el inicio de sesión (no funcional en este ejemplo)
        TextField(value = "", onValueChange = {}, label = { Text("Usuario") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = "", onValueChange = {}, label = { Text("Contraseña") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("home_screen") }) {
            Text(text = "Iniciar Sesión")
        }
    }
}

// Pantalla de configuración de notificaciones
@Composable
fun NotificationsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Configuración de Notificaciones", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Simulación de opciones de notificaciones
        var notificationsEnabled by remember { mutableStateOf(true) }
        var soundEnabled by remember { mutableStateOf(true) }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Activar Notificaciones")
            Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Sonido de Notificaciones")
            Switch(checked = soundEnabled, onCheckedChange = { soundEnabled = it })
        }
    }
}

