package com.example.datossinmvvm

import androidx.compose.runtime.mutableStateListOf

object RewardRepository {
    private val rewards = mutableStateListOf<Reward>(
        Reward(id = "1", title = "Recompensa 1", description = "Descripción de la recompensa 1", points = 100),
        Reward(id = "2", title = "Recompensa 2", description = "Descripción de la recompensa 2", points = 200)
    )

    fun getRewards(): List<Reward> = rewards

    fun unlockReward(id: String) {
        rewards.find { it.id == id }?.isUnlocked = true
    }
}