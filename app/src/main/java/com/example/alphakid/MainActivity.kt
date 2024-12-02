package com.example.alphakid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
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
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.random.Random
import androidx.compose.material3.TopAppBarDefaults

class MainActivity : ComponentActivity() {

    private val apiService: ApiService by lazy {
        RetrofitInstance.retrofit.create(ApiService::class.java)
    }

    private var detectedText by mutableStateOf("")
    private var challengeText by mutableStateOf("")
    private var randomPalabra by mutableStateOf<Palabra?>(null)
    private var wordCount by mutableStateOf(0)
    private var isProcessing by mutableStateOf(false)
    private var lastChallengeWord by mutableStateOf("")
    private var points by mutableStateOf(0)
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlphakidTheme {
                navController = rememberNavController()
                AppNavHost(navController)
            }
        }

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        // Load points from SharedPreferences
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        points = sharedPreferences.getInt("user_points", 0)
    }

    @Composable
    fun AppNavHost(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "main") {
            composable("main") { MainScreen(navController) }
            composable("wordDetail") { randomPalabra?.let { WordDetailScreen(it, navController) { startCameraForScan() } } }
            composable("result") {
                ResultScreen(navController, detectedText, challengeText, wordCount, randomPalabra) {
                    startChallenge(navController)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(navController: NavHostController) {
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", "Usuario Anónimo")
        val profileImage = sharedPreferences.getInt("profile_image", -1)
        val points = sharedPreferences.getInt("user_points", 0)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (profileImage != -1) {
                                Image(
                                    painter = painterResource(id = profileImage),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Column {
                                Text(text = "¡Bienvenido, $userName!", color = Color.White)
                                Text(text = "Puntos: $points", color = Color.White)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xFF314673)
                    )
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { startCameraForScan() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Escanear Texto", color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { startChallenge(navController) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Iniciar Reto", color = MaterialTheme.colorScheme.onSecondary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (lastChallengeWord.isNotEmpty()) {
                    Text(
                        text = "Última palabra del reto: $lastChallengeWord",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }

    private fun startCameraForScan() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun startChallenge(navController: NavHostController) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val palabras = apiService.getPalabras()
                if (palabras.isNotEmpty()) {
                    randomPalabra = palabras[Random.nextInt(palabras.size)]
                    challengeText = randomPalabra?.nombre ?: ""
                    detectedText = ""
                    withContext(Dispatchers.Main) {
                        navController.navigate("wordDetail")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Error al buscar la palabra: ${e.message}")
                }
            }
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
                println("Acceso a la camara denegado")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                isProcessing = true
                processImage(imageBitmap)
            } else {
                println("Lo lamentamos, no pudimos capturar la imagen.")
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val credentials = BasicAWSCredentials("-", "-") // Add your AWS credentials
                val rekognitionClient = AmazonRekognitionClient(credentials)
                rekognitionClient.setRegion(Region.getRegion(Regions.US_EAST_1))

                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val imageBytes = ByteBuffer.wrap(stream.toByteArray())
                val image = Image().withBytes(imageBytes)

                val request = DetectTextRequest().withImage(image)
                val result: DetectTextResult = rekognitionClient.detectText(request)

                val detectedText = result.textDetections.firstOrNull()?.detectedText ?: "No se detecto el texto"
                withContext(Dispatchers.Main) {
                    this@MainActivity.detectedText = detectedText
                    isProcessing = false
                    navController.navigate("resultado") // Ensure navigation to result screen
                }
            } catch (e: Exception) {
                println("Error al procesar la imagen: ${e.message}")
                isProcessing = false
            }
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CAMERA_PERMISSION = 2
    }
}