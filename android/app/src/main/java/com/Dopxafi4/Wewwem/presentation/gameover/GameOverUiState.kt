package com.Dopxafi4.Wewwem.presentation.gameover

import androidx.annotation.StringRes
import com.Dopxafi4.Wewwem.R

data class GameOverUiState(
    val banked: Boolean = false,
    val tokens: Int = 0,
    val steps: Int = 0,
    val totalSteps: Int = 0,
    @StringRes val routeTitleRes: Int = R.string.route_bamboo,
    val bestSteps: Int = 0,
    val newRecord: Boolean = false
) {
    val visibleStats: Int
        get() = listOf(tokens > 0, steps > 0, bestSteps > 0).count { it }
}
