package com.Dopxafi4.Wewwem.domain.model

data class PlayerProfile(
    val lifetimeTokens: Int,
    val totalRuns: Int,
    val shields: Int,
    val bestSteps: Map<String, Int>,
    val claimedTiers: Set<String>,
    val unlockedCharms: Set<String>
) {
    fun bestOn(routeId: String): Int = bestSteps[routeId] ?: 0
}
