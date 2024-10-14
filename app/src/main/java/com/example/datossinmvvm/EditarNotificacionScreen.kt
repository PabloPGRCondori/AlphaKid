package com.example.datossinmvvm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarNotificacionScreen(
    navController: NavController,
    index: Int,
    notificacionActual: Pair<String, String>,
    onUpdateNotificacion: (Int, Pair<String, String>) -> Unit
) {
    var titulo by remember { mutableStateOf(notificacionActual.first) }
    var descripcion by remember { mutableStateOf(notificacionActual.second  ) }

    val primaryColor = Color(0xFF739491)
    val secondaryColor = Color(0xFF4D88B3)
    val tertiaryColor = Color(0xFF314673)
    val backgroundColor = Color(0xFFE3E6D8)
    val accentColor = Color(0xFF7DB0D6)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Notificación", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Campo de texto para el título
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título", color = tertiaryColor) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = secondaryColor,
                    unfocusedBorderColor = tertiaryColor,
                    cursorColor = secondaryColor,
                    focusedLabelColor = secondaryColor,
                    unfocusedLabelColor = tertiaryColor
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para la descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción", color = tertiaryColor) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = secondaryColor,
                    unfocusedBorderColor = tertiaryColor,
                    cursorColor = secondaryColor,
                    focusedLabelColor = secondaryColor,
                    unfocusedLabelColor = tertiaryColor
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar los cambios
            Button(
                onClick = {
                    // Actualizamos la notificación con el nuevo título y descripción
                    onUpdateNotificacion(index, Pair(titulo, descripcion))
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text("Guardar Cambios", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditarNotificacionScreenPreview() {
    val navController = rememberNavController()
    EditarNotificacionScreen(
        navController = navController,
        index = 0,
        notificacionActual = Pair("Título de Ejemplo", "Descripción de Ejemplo"),
        onUpdateNotificacion = { _, _ -> }
    )
}