package com.Dopxafi4.Wewwem.presentation.daily

import androidx.annotation.StringRes
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.domain.model.DailyGoal

data class DailyChallengeUiState(
    val goal: DailyGoal = DailyGoal.COMPLETE_RUNS,
    val target: Int = 0,
    val progress: Int = 0,
    val fraction: Float = 0f,
    @StringRes val routeTitleRes: Int = R.string.route_bamboo,
    val completed: Boolean = false,
    val claimed: Boolean = false,
    val shields: Int = 0
)
