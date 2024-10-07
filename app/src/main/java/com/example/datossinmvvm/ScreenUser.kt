package com.example.datossinmvvm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current
    var id        by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    val dataUser  = remember { mutableStateOf("") }

    val db: UserDatabase = crearDatabase(context)
    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios") },
                actions = {
                    Button(onClick = {
                        val user = User(0, firstName, lastName)
                        coroutineScope.launch {
                            AgregarUsuario(user = user, dao = dao)
                        }
                        firstName = ""
                        lastName = ""
                    }) {
                        Text("Agregar Usuario")
                    }
                    Button(onClick = {
                        coroutineScope.launch {
                            val data = getUsers(dao = dao)
                            dataUser.value = data
                        }
                    }) {
                        Text("Listar Usuarios")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(50.dp))
            TextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID (solo lectura)") },
                readOnly = true,
                singleLine = true
            )
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name: ") },
                singleLine = true
            )
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name:") },
                singleLine = true
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        EliminarUltimoUsuario(dao = dao)
                        val data = getUsers(dao = dao)
                        dataUser.value = data
                    }
                }
            ) {
                Text("Eliminar Usuario", fontSize = 16.sp)
            }
            Text(
                text = dataUser.value, fontSize = 20.sp
            )
        }
    }
}

@Composable
fun crearDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()
}

suspend fun getUsers(dao: UserDao): String {
    var rpta: String = ""
    val users = dao.getAll()
    users.forEach { user ->
        val fila = "${user.firstName} - ${user.lastName}\n"
        rpta += fila
    }
    return rpta
}

suspend fun AgregarUsuario(user: User, dao: UserDao) {
    try {
        dao.insert(user)
    } catch (e: Exception) {
        Log.e("User", "Error: insert: ${e.message}")
    }
}

suspend fun EliminarUltimoUsuario(dao: UserDao) {
    try {
        dao.deleteLastUser()
    } catch (e: Exception) {
        Log.e("User", "Error: deleteLastUser: ${e.message}")
    }
}