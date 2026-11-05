package com.Dopxafi4.Wewwem.presentation.common

import androidx.annotation.StringRes

data class RouteUi(
    val id: String,
    @StringRes val titleRes: Int,
    val tiles: Int,
    val rows: Int,
    val baseReward: Int,
    val unlockTokens: Int,
    val locked: Boolean,
    val bestSteps: Int,
    val remainingTokens: Int
)
