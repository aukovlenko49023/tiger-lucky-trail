package com.Dopxafi4.Wewwem.presentation.progress

import com.Dopxafi4.Wewwem.presentation.common.RouteUi

data class ProgressUiState(
    val lifetimeTokens: Int = 0,
    val totalRuns: Int = 0,
    val bestSteps: Int = 0,
    val routes: List<RouteUi> = emptyList()
) {
    val visibleStats: Int
        get() = listOf(lifetimeTokens > 0, totalRuns > 0, bestSteps > 0).count { it }
}
