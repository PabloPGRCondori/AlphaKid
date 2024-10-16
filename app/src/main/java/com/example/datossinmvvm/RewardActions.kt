package com.example.datossinmvvm

fun onUserActionCompleted(actionId: String) {
    // Lógica para determinar qué recompensa desbloquear
    when (actionId) {
        "action1" -> RewardRepository.unlockReward("1")
        "action2" -> RewardRepository.unlockReward("2")
        // Agrega más acciones y recompensas según sea necesario
    }
}