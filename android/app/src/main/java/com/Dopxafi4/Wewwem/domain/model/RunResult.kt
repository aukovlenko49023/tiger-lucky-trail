package com.Dopxafi4.Wewwem.domain.model

data class RunResult(
    val routeId: String,
    val banked: Boolean,
    val tokens: Int,
    val steps: Int,
    val totalSteps: Int,
    val previousBest: Int
) {
    val newRecord: Boolean get() = steps > previousBest
}
