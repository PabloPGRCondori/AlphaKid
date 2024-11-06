package com.example.alphakid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.DetectLabelsRequest
import com.amazonaws.services.rekognition.model.DetectLabelsResult
import com.amazonaws.services.rekognition.model.Image
import com.example.alphakid.ui.theme.AlphakidTheme
import java.io.File
import java.nio.ByteBuffer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlphakidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Inicializar el cliente de Rekognition
        val credentials = BasicAWSCredentials("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY")
        val rekognitionClient = AmazonRekognitionClient(credentials)
        rekognitionClient.setRegion(Region.getRegion(Regions.US_EAST_1))

        // Cargar una imagen desde los recursos
        val imageFile = File(filesDir, "test-image.png")
        val imageBytes = ByteBuffer.wrap(imageFile.readBytes())
        val image = Image().withBytes(imageBytes)

        // Crear la solicitud de detección de etiquetas
        val request = DetectLabelsRequest().withImage(image).withMaxLabels(10).withMinConfidence(75F)

        // Ejecutar la solicitud
        val result: DetectLabelsResult = rekognitionClient.detectLabels(request)

        // Procesar el resultado
        result.labels.forEach { label ->
            println("Detected label: ${label.name} with confidence: ${label.confidence}")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlphakidTheme {
        Greeting("Android")
    }
}