package com.Dopxafi4.Wewwem.presentation.gameover

import androidx.lifecycle.ViewModel
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import com.Dopxafi4.Wewwem.presentation.common.Labels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameOverViewModel(
    private val trailRepository: TrailRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        val result = trailRepository.lastRunResult() ?: return
        val profile = trailRepository.profile()
        _state.value = GameOverUiState(
            banked = result.banked,
            tokens = result.tokens,
            steps = result.steps,
            totalSteps = result.totalSteps,
            routeTitleRes = Labels.route(result.routeId),
            bestSteps = profile.bestOn(result.routeId),
            newRecord = result.newRecord
        )
    }
}
