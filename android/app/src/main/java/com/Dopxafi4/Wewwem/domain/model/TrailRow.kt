package com.Dopxafi4.Wewwem.domain.model

data class TrailRow(
    val hazards: Set<Int>,
    val stones: List<StoneState>
) {
    fun firstSafeIndex(): Int = stones.indices.firstOrNull { it !in hazards } ?: -1
}
