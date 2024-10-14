import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.datossinmvvm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizarAlertasCRUDScreen(
    navController: NavController,
    notificaciones: List<Pair<String, String>>,
    onUpdateNotificaciones: (List<Pair<String, String>>) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Usamos un Row para centrar el título
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { /* Acción para el icono local */ },
                            modifier = Modifier
                                .size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu_07),
                                contentDescription = "Descripción del icono"
                            )
                        }

                        // Título centrado
                        Text(
                            text = "Personalizar alertas",
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        // Icono de la derecha (puedes cambiarlo por uno local)
                        IconButton(onClick = {
                            // Acción para el icono de la derecha
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.lapiz),
                                contentDescription = "Descripción del icono"
                            )
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("crear_notificacion") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Notificación")
            }
        },
        bottomBar = {
            IconosInferiores()
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NotificacionesCRUD(
                navController = navController,
                notificaciones = notificaciones,
                updateNotificaciones = onUpdateNotificaciones
            )
        }
    }
}

@Composable
fun NotificacionesCRUD(
    navController: NavController,
    notificaciones: List<Pair<String, String>>,
    updateNotificaciones: (List<Pair<String, String>>) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        notificaciones.forEachIndexed { index, (titulo, descripcion) ->
            var isChecked by remember { mutableStateOf(false) }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = titulo,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = descripcion,
                            fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    IconButton(onClick = {
                        navController.navigate("editar_notificacion/$index")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar notificación")
                    }
                    IconButton(onClick = {
                        updateNotificaciones(notificaciones.toMutableList().apply { removeAt(index) })
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar notificación")
                    }
                }
            }
        }
    }
}

@Composable
fun IconosInferiores() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Acción del icono */ }) {
            Image(
                painter = painterResource(id = R.drawable.nota_musical),
                contentDescription = "Ringtones",
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { /* Acción del icono */ }) {
            Image(
                painter = painterResource(id = R.drawable.reproducir),
                contentDescription = "Reproducir",
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { /* Acción del icono */ }) {
            Image(
                painter = painterResource(id = R.drawable.favorite_lab6),
                contentDescription = "Favorito",
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { /* Acción del icono */ }) {
            Image(
                painter = painterResource(id = R.drawable.descarga_7),
                contentDescription = "Descarga",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalizarAlertasCRUDScreenPreview() {
    val navController = rememberNavController()
    PersonalizarAlertasCRUDScreen(
        navController = navController,
        notificaciones = listOf(Pair("Título de prueba", "Descripción de prueba")),
        onUpdateNotificaciones = {}
    )
}