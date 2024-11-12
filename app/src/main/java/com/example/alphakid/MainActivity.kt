package com.example.alphakid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.alphakid.ui.theme.accentColor
import com.example.alphakid.ui.theme.backgroundColor
import com.example.alphakid.ui.theme.primaryColor
import com.example.alphakid.ui.theme.secondaryColor
import com.example.alphakid.ui.theme.tertiaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val apiService: ApiService by lazy {
        RetrofitInstance.retrofit.create(ApiService::class.java)
    }

    private var detectedText by mutableStateOf("")
    private var challengeText by mutableStateOf("")
    private var randomPalabra by mutableStateOf<Palabra?>(null)
    private var wordCount by mutableStateOf(0)
    private var showContinueButton by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlphakidTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.principal_icon), // Add a logo image
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(75.dp))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Detected text: $detectedText",
                            style = MaterialTheme.typography.headlineMedium,
                            color = tertiaryColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { startCameraForScan() },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text(text = "Scan Text", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (showContinueButton) {
                            Button(
                                onClick = { startChallenge() },
                                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                            ) {
                                Text(text = "Continue Challenge", color = Color.White)
                            }
                        } else {
                            Button(
                                onClick = { startChallenge() },
                                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                            ) {
                                Text(text = "Start Challenge", color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = challengeText,
                            style = MaterialTheme.typography.headlineMedium,
                            color = accentColor
                        )
                    }
                }
            }
        }

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun startCameraForScan() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun startChallenge() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val palabras = apiService.getPalabras()
                if (palabras.isNotEmpty()) {
                    randomPalabra = palabras[Random.nextInt(palabras.size)]
                    withContext(Dispatchers.Main) {
                        challengeText = "Form the word ${randomPalabra?.palabra}"
                        showContinueButton = false
                        startTimer()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Error fetching words: ${e.message}")
                }
            }
        }
    }

    private fun startTimer() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                challengeText = "Form the word ${randomPalabra?.palabra} in ${millisUntilFinished / 1000} seconds"
            }

            override fun onFinish() {
                showContinueButton = true
                challengeText = "Time's up! Form the word ${randomPalabra?.palabra}"
                startCameraForScan()
            }
        }.start()
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
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                processImage(imageBitmap)
            } else {
                println("Error: Image capture failed")
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
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
                withContext(Dispatchers.Main) {
                    this@MainActivity.detectedText = detectedText

                    if (challengeText.isNotEmpty()) {
                        if (detectedText.equals(randomPalabra?.palabra, ignoreCase = true)) {
                            wordCount++
                            if (wordCount >= 10) {
                                challengeText = "Congratulations! You have completed 10 words."
                            } else {
                                challengeText = "Congratulations! You formed the word ${randomPalabra?.palabra} correctly."
                                showContinueButton = true
                            }
                        } else {
                            challengeText = "Incorrect or poorly focused. Try again."
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error processing image: ${e.message}")
            }
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CAMERA_PERMISSION = 2
    }
}