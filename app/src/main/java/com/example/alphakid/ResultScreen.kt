package com.example.alphakid

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

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
    val scope = rememberCoroutineScope()
    var showResult by remember { mutableStateOf(false) }

    LaunchedEffect(detectedText) {
        if (randomPalabra != null) {
            if (detectedText.equals(randomPalabra.nombre, ignoreCase = true)) {
                mutableWordCount++
                val wordPoints = randomPalabra.puntos
                val sharedPreferences =
                    context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val currentPoints = sharedPreferences.getInt("user_points", 0)
                with(sharedPreferences.edit()) {
                    putInt("user_points", currentPoints + wordPoints)
                    apply()
                }
                if (mutableWordCount >= 10) {
                    resultMessage = "¡Lo lograste! Has escaneado 10 palabras correctamente."
                    showContinueButton = false
                    showResult = true
                    navController.navigate("main") // Navigate back to the main screen
                } else {
                    resultMessage =
                        "¡Felicidades! Formaste la palabra ${randomPalabra.nombre} correctamente y ganaste $wordPoints puntos."
                    showContinueButton = true
                    showResult = true
                }
            } else {
                resultMessage =
                    "Texto detectado: $detectedText. No coincide con la palabra del reto."
            }
        } else {
            resultMessage = "No se encontró una palabra del reto."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.LightGray,
                        Color.White
                    )
                )
            )
    ) {
        // Primera Card para la "Palabra del reto"
        if (randomPalabra != null) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Palabra del reto: ${randomPalabra.nombre}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre las Cards
        }

        // Segunda Card para el mensaje de resultado y los botones
        AnimatedVisibility(
            visible = showResult,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = resultMessage,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }

        }
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 16.dp), // Espacio inferior para la imagen
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre botones
        ) {
            if (showContinueButton) {
                Button(
                    onClick = { onContinue() },
                    modifier = Modifier
                        .weight(1f) // Ocupar la mitad del espacio disponible
                        .padding(8.dp)
                ) {
                    Text(text = "Continuar")
                }
            }
            Button(
                onClick = { navController.navigate("main") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = "Salir")
            }

        }
        Image(
            painter = painterResource(id = R.drawable.tu_imagen),
            contentDescription = "Imagen",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp) //
        )
        LaunchedEffect(resultMessage) {
            if (resultMessage.isNotEmpty()) {
                showResult = true
            }
        }
    }
}