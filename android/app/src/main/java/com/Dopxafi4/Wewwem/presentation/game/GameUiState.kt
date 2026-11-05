package com.Dopxafi4.Wewwem.presentation.game

import androidx.annotation.StringRes
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.domain.model.TrailRow

enum class GamePhase {
    READY,
    CHOOSING,
    RESOLVING,
    BLOCKED,
    FINISHED
}

sealed interface GameEvent {
    data object Finished : GameEvent
}

data class GameUiState(
    val routeId: String = SampleData.ROUTE_BAMBOO,
    @StringRes val routeTitleRes: Int = R.string.route_bamboo,
    val rows: List<TrailRow> = emptyList(),
    val rowRewards: List<Int> = emptyList(),
    val totalRows: Int = 0,
    val tiles: Int = 3,
    val activeRow: Int = 0,
    val tigerRow: Int = -1,
    val tigerColumn: Int = 1,
    val runTokens: Int = 0,
    val bankedTotal: Int = 0,
    val shields: Int = 0,
    val step: Int = 0,
    val nextReward: Int = 0,
    val phase: GamePhase = GamePhase.READY,
    @StringRes val message: Int? = null,
    val hintRow: Int = -1,
    val hintColumn: Int = -1,
    val reducedMotion: Boolean = false,
    val haptics: Boolean = false,
    val leapTick: Int = 0,
    val shakeTick: Int = 0,
    val gainTick: Int = 0
) {
    val inputEnabled: Boolean
        get() = phase == GamePhase.READY || phase == GamePhase.CHOOSING

    val canBank: Boolean
        get() = inputEnabled && runTokens > 0
}
