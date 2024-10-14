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
fun AppNavigation() {
    val navController = rememberNavController()

    var notificaciones by remember { mutableStateOf(listOf<Pair<String, String>>()) }

    val primaryColor = Color(0xFF739491)
    val secondaryColor = Color(0xFF4D88B3)
    val tertiaryColor = Color(0xFF314673)
    val backgroundColor = Color(0xFFE3E6D8)
    val accentColor = Color(0xFF7DB0D6)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Notificaciones", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "lista_notificaciones",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
        ) {
            composable("lista_notificaciones") {
                PersonalizarAlertasCRUDScreen(
                    navController = navController,
                    notificaciones = notificaciones,
                    onUpdateNotificaciones = { updatedList ->
                        notificaciones = updatedList
                    }
                )
            }
            composable(
                "editar_notificacion/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            ) { backStackEntry ->
                val index = backStackEntry.arguments?.getInt("index") ?: 0
                val notificacionActual = notificaciones.getOrNull(index)

                if (notificacionActual != null) {
                    EditarNotificacionScreen(
                        navController = navController,
                        index = index,
                        notificacionActual = notificacionActual,
                        onUpdateNotificacion = { i, updatedNotificacion ->
                            notificaciones = notificaciones.toMutableList().apply {
                                set(i, updatedNotificacion)
                            }
                        }
                    )
                }
            }
            composable("crear_notificacion") {
                CrearNotificacionScreen(
                    navController = navController,
                    onCreateNotificacion = { newNotificacionTitle, newNotificacionDescription ->
                        notificaciones = notificaciones + Pair(newNotificacionTitle, newNotificacionDescription)
                    }
                )
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