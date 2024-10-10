import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.datossinmvvm.CrearNotificacionScreen
import com.example.datossinmvvm.EditarNotificacionScreen
import com.example.datossinmvvm.PersonalizarAlertasCRUDScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearNotificacionScreen(
    navController: NavController,
    onCreateNotificacion: (String, String) -> Unit
) {
    var newNotificacionTitle by remember { mutableStateOf("") }
    var newNotificacionDescription by remember { mutableStateOf("") }
    val primaryColor = Color(0xFF739491)
    val secondaryColor = Color(0xFF4D88B3)
    val tertiaryColor = Color(0xFF314673)
    val backgroundColor = Color(0xFFE3E6D8)
    val accentColor = Color(0xFF7DB0D6)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Notificación", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = newNotificacionTitle,
                onValueChange = { newNotificacionTitle = it },
                label = { Text("Título de la Notificación") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = secondaryColor,
                    unfocusedBorderColor = tertiaryColor,
                    cursorColor = secondaryColor,
                    focusedLabelColor = secondaryColor,
                    unfocusedLabelColor = tertiaryColor,
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.Black) // Ajuste del color del texto
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newNotificacionDescription,
                onValueChange = { newNotificacionDescription = it },
                label = { Text("Descripción de la Notificación") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = secondaryColor,
                    unfocusedBorderColor = tertiaryColor,
                    cursorColor = secondaryColor,
                    focusedLabelColor = secondaryColor,
                    unfocusedLabelColor = tertiaryColor
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.Black) // Ajuste del color del texto
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onCreateNotificacion(newNotificacionTitle, newNotificacionDescription)
                    navController.navigate("lista_notificaciones") // Navegar a la lista de notificaciones
                },
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Crear", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear", color = Color.White)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}