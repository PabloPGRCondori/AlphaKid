package com.example.datossinmvvm

import androidx.compose.runtime.mutableStateListOf

object RewardRepository {
    private val rewards = mutableStateListOf<Reward>()

    fun getRewards(): List<Reward> = rewards

    fun addReward(reward: Reward) {
        rewards.add(reward)
    }

    fun removeReward(reward: Reward) {
        rewards.remove(reward)
    }

    fun updateReward(updatedReward: Reward) {
        val index = rewards.indexOfFirst { it.id == updatedReward.id }
        if (index != -1) {
            rewards[index] = updatedReward
        }
    }
}