package com.example.datossinmvvm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun RewardScreen(navController: NavController, rewards: List<Reward>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Recompensas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        rewards.forEach { reward ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = { /* Navegar a detalles de la recompensa */ }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(reward.title, style = MaterialTheme.typography.headlineLarge)
                    Text(reward.description, style = MaterialTheme.typography.bodyMedium)
                    Text("Puntos: ${reward.points}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRewardScreen() {
    val navController = rememberNavController()
    val sampleRewards = listOf(
        Reward(id = "1", title = "Recompensa 1", description = "Descripción de la recompensa 1", points = 100),
        Reward(id = "2", title = "Recompensa 2", description = "Descripción de la recompensa 2", points = 200)
    )
    RewardScreen(navController, sampleRewards)
}