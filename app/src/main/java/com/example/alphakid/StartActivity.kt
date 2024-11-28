package com.example.alphakid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.alphakid.ui.theme.AlphakidTheme
import kotlin.random.Random

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlphakidTheme {
                StartScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StartScreen() {
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ingrese su nombre") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val randomImageId = getRandomProfileImage()
                with(sharedPreferences.edit()) {
                    putString("user_name", name)
                    putInt("profile_image", randomImageId)
                    putInt("user_points", 0) // Initialize points to 0
                    apply()
                }
                context.startActivity(Intent(context, MainActivity::class.java))
            }) {
                Text("Continuar")
            }
        }
    }
}

fun getRandomProfileImage(): Int {
    val images = listOf(
        R.drawable.emojione_v1__ewe,
        R.drawable.noto_v1__face_with_tongue,
        R.drawable.emojione_v1__baby_angel,
        R.drawable.emojione_v1__chicken,
        R.drawable.emojione_v1__mushroom,
        R.drawable.emojione_v1__boy,
        R.drawable.emojione_v1__bright_button,
        R.drawable.emojione_v1__dove,
        R.drawable.emojione_v1__dolphin,
        R.drawable.emojione_v1__front_facing_baby_chick,
        R.drawable.emojione_v1__grinning_squinting_face,
        R.drawable.emojione_v1__beaming_face_with_smiling_eyes,
        R.drawable.noto_v1__cat_with_wry_smile,
        R.drawable.noto_v1__cowboy_hat_face,
        R.drawable.noto_v1__pig,
    )
    return images[Random.nextInt(images.size)]
}