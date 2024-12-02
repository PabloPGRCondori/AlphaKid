package com.example.alphakid

import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter

@Composable
fun WordDetailScreen(palabra: Palabra, navController: NavHostController, onTakePhoto: () -> Unit) {
    var timerText by remember { mutableStateOf("Forma la palabra en 5 segundos") }
    var isProcessing by remember { mutableStateOf(false) }
    var timerFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText = "Forma la palabra en  ${millisUntilFinished / 1000} segundos"
            }

            override fun onFinish() {
                timerText = "EMPEZEMOS!!!!"
                timerFinished = true
            }
        }.start()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFE3E6D8), Color(0xFF7DB0D6))))
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 150.dp),
            shape = RoundedCornerShape(0.dp), // Recuadro sin puntas
            colors = CardDefaults.cardColors(containerColor = Color(0xFF7DB0D6)) // Color del recuadro E3E6D8
        ) {
            Text(
                text = timerText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontSize = 24.sp, // Tamaño de fuente aumentado
                modifier = Modifier.padding(8.dp) // Padding interno del recuadro
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = true, // La imagen siempre es visible
                enter = fadeIn(animationSpec = spring())
            ) {
                Image(
                    painter = rememberImagePainter(palabra.imagen),
                    contentDescription = "Imagen de la palabra",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = palabra.nombre,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Default
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Puntos: ${palabra.puntos}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (isProcessing) {
                CircularProgressIndicator()
                Text(
                    text = "Processing, please wait...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onTakePhoto,
                        modifier = Modifier
                            .size(80.dp) // Botón grande
                            .background(Color(0xFF4D88B3)), // Color 4D88B3
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D88B3)),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt, // Icono de cámara
                            contentDescription = "Take Photo"
                        )
                    }
                    Button(
                        onClick = { navController.navigate("main") },
                        modifier = Modifier
                            .size(80.dp) // Botón grande
                            .background(Color(0xFF4D88B3)), // Color 4D88B3
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D88B3)),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp, // Icono de salida
                            contentDescription = "Salir del Reto"
                        )
                    }
                }
            }
        }
    }
}