package com.example.alphakid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.DetectTextRequest
import com.amazonaws.services.rekognition.model.DetectTextResult
import com.amazonaws.services.rekognition.model.Image
import com.example.alphakid.ui.theme.AlphakidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import android.media.MediaPlayer


class MainActivity : ComponentActivity() {

    private var detectedText by mutableStateOf("")
    private var challengeText by mutableStateOf("")
    private var points by mutableStateOf(0)
    private var currentWordIndex by mutableStateOf(0)
    private val words = listOf("BALSA", "TECLA", "BARCO", "PELOTA", "COSTAL", "CALDERA", "BOLSA", "TAPETE", "RELOJ", "DORADO")
    private val wordPoints = mapOf(
        "BALSA" to 5,
        "TECLA" to 10,
        "BARCO" to 15,
        "PELOTA" to 20,
        "COSTAL" to 25,
        "CALDERA" to 30,
        "BOLSA" to 35,
        "TAPETE" to 40,
        "RELOJ" to 45,
        "DORADO" to 50
    )
    private lateinit var correctSound: MediaPlayer
    private lateinit var incorrectSound: MediaPlayer
    private lateinit var backgroundMusic: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlphakidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Greeting(name = detectedText)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { startCameraForScan() }) {
                            Text(text = "Scan Text")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { startChallenge() }) {
                            Text(text = "Start Challenge")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = challengeText)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Points: $points")
                        Spacer(modifier = Modifier.height(16.dp))
                        if (challengeText.isNotEmpty()) {
                            Button(onClick = { continueChallenge() }) {
                                Text(text = "Continue")
                            }
                        }
                    }
                }
            }
        }

        // Initialize MediaPlayer for sounds
        correctSound = MediaPlayer.create(this, R.raw.correcto)
        incorrectSound = MediaPlayer.create(this, R.raw.incorrecto)
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
        backgroundMusic.isLooping = true
        backgroundMusic.start()

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun startCameraForScan() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun startChallenge() {
        currentWordIndex = 0
        points = 0
        challengeText = words[currentWordIndex]
        startCameraForScan()
    }

    private fun continueChallenge() {
        if (currentWordIndex < words.size - 1) {
            currentWordIndex++
            challengeText = words[currentWordIndex]
            startCameraForScan()
        } else {
            challengeText = "Congratulations! You completed the challenge with $points points."
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, start the camera
                startCameraForScan()
            } else {
                // Permission denied, handle accordingly
                println("Camera permission denied")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            processImage(imageBitmap)
        }
    }

    private fun processImage(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            // Replace YOUR_ACCESS_KEY and YOUR_SECRET_KEY with your AWS credentials
            val credentials = BasicAWSCredentials("-", "-")
            val rekognitionClient = AmazonRekognitionClient(credentials)
            rekognitionClient.setRegion(Region.getRegion(Regions.US_EAST_1))

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageBytes = ByteBuffer.wrap(stream.toByteArray())
            val image = Image().withBytes(imageBytes)

            val request = DetectTextRequest().withImage(image)
            val result: DetectTextResult = rekognitionClient.detectText(request)

            val detectedText = result.textDetections.firstOrNull()?.detectedText ?: "No text detected"
            this@MainActivity.detectedText = detectedText

            if (challengeText.isNotEmpty() && currentWordIndex < words.size) {
                if (detectedText.equals(challengeText, ignoreCase = true)) {
                    val pointsEarned = wordPoints[challengeText] ?: 0
                    this@MainActivity.points += pointsEarned
                    this@MainActivity.challengeText = "Correct! You earned $pointsEarned points."
                } else {
                    this@MainActivity.challengeText = "Incorrect or poorly focused. Try again."
                }
            }
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CAMERA_PERMISSION = 2
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Detected text: $name",
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