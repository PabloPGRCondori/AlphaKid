package com.example.alphakid

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ResultScreen(
    navController: NavHostController,
    detectedText: String,
    challengeText: String,
    wordCount: Int,
    randomPalabra: Palabra?,
    onContinue: () -> Unit
) {
    var resultMessage by remember { mutableStateOf("") }
    var showContinueButton by remember { mutableStateOf(false) }
    var mutableWordCount by remember { mutableStateOf(wordCount) }
    val context = LocalContext.current

    LaunchedEffect(detectedText) {
        if (randomPalabra != null) {
            if (detectedText.equals(randomPalabra.nombre, ignoreCase = true)) {
                mutableWordCount++
                val wordPoints = randomPalabra.puntos
                val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val currentPoints = sharedPreferences.getInt("user_points", 0)
                with(sharedPreferences.edit()) {
                    putInt("user_points", currentPoints + wordPoints)
                    apply()
                }
                if (mutableWordCount >= 10) {
                    resultMessage = "¡Lo lograste! Has escaneado 10 palabras correctamente."
                    showContinueButton = false
                    navController.navigate("main") // Navigate back to the main screen
                } else {
                    resultMessage = "¡Felicidades! Formaste la palabra ${randomPalabra.nombre} correctamente y ganaste $wordPoints puntos."
                    showContinueButton = true
                }
            } else {
                resultMessage = "Texto detectado: $detectedText. No coincide con la palabra del reto."
            }
        } else {
            resultMessage = "No se encontró una palabra del reto."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (randomPalabra != null) {
            Text(
                text = "Palabra del reto: ${randomPalabra.nombre}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = resultMessage,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (showContinueButton) {
            Button(onClick = { onContinue() }) {
                Text(text = "Continuar")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("main") }) {
            Text(text = "Salir")
        }
    }
}