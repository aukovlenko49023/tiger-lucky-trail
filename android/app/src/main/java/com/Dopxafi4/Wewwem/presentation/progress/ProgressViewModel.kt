package com.Dopxafi4.Wewwem.presentation.progress

import androidx.lifecycle.ViewModel
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import com.Dopxafi4.Wewwem.domain.usecase.UnlockRoutesUseCase
import com.Dopxafi4.Wewwem.presentation.common.Labels
import com.Dopxafi4.Wewwem.presentation.common.RouteUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressViewModel(
    private val trailRepository: TrailRepository,
    private val unlockRoutesUseCase: UnlockRoutesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val profile = trailRepository.profile()
        val tokens = profile.lifetimeTokens
        _state.value = ProgressUiState(
            lifetimeTokens = tokens,
            totalRuns = profile.totalRuns,
            bestSteps = profile.bestSteps.values.maxOrNull() ?: 0,
            routes = SampleData.routes.map { route ->
                RouteUi(
                    id = route.id,
                    titleRes = Labels.route(route.id),
                    tiles = route.tiles,
                    rows = route.rows,
                    baseReward = route.baseReward,
                    unlockTokens = route.unlockTokens,
                    locked = !unlockRoutesUseCase(route, tokens),
                    bestSteps = profile.bestOn(route.id),
                    remainingTokens = unlockRoutesUseCase.remaining(route, tokens)
                )
            }
        )
    }
}
