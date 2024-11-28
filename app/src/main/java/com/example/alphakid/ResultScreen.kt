package com.example.alphakid

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    LaunchedEffect(detectedText) {
        if (randomPalabra != null) {
            if (detectedText.equals(randomPalabra.nombre, ignoreCase = true)) {
                mutableWordCount++
                if (mutableWordCount >= 10) {
                    resultMessage = "¡Logrado! Has completado 10 palabras."
                } else {
                    resultMessage = "¡Felicidades! Formaste la palabra ${randomPalabra.nombre} correctamente."
                    showContinueButton = true
                }
            } else {
                resultMessage = "Incorrecto o mal enfocado. Inténtalo de nuevo."
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Texto detectado: $detectedText",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = resultMessage,
            style = MaterialTheme.typography.bodyLarge
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