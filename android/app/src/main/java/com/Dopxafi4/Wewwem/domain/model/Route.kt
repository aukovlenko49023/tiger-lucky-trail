package com.Dopxafi4.Wewwem.domain.model

data class Route(
    val id: String,
    val tiles: Int,
    val hazards: Int,
    val rows: Int,
    val baseReward: Int,
    val growth: Int,
    val summitBonus: Int,
    val unlockTokens: Int
) {
    fun stepReward(step: Int): Int = baseReward + growth * (step - 1)
}
