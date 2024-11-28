package com.example.alphakid

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter

@Composable
fun WordDetailScreen(palabra: Palabra, navController: NavHostController, onTakePhoto: () -> Unit) {
    var timerText by remember { mutableStateOf("Form the word in 5 seconds") }
    var isProcessing by remember { mutableStateOf(false) }
    var timerFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText = "Form the word in ${millisUntilFinished / 1000} seconds"
            }

            override fun onFinish() {
                timerText = "Time's up! Taking a photo to scan the word."
                timerFinished = true
                onTakePhoto()
            }
        }.start()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(palabra.imagen),
            contentDescription = "Imagen de la palabra",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = palabra.nombre,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Puntos: ${palabra.puntos}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = timerText,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Red
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
                Button(onClick = onTakePhoto) {
                    Text(text = "Take Photo")
                }
                Button(onClick = { navController.navigate("main") }) {
                    Text(text = "Salir del Reto")
                }
            }
        }
    }
}