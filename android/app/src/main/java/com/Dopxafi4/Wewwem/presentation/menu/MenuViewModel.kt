package com.Dopxafi4.Wewwem.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import com.Dopxafi4.Wewwem.domain.usecase.DailyChallengeUseCase
import com.Dopxafi4.Wewwem.domain.usecase.UnlockRoutesUseCase
import com.Dopxafi4.Wewwem.presentation.common.Labels
import com.Dopxafi4.Wewwem.presentation.common.RouteUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val trailRepository: TrailRepository,
    private val dailyChallengeUseCase: DailyChallengeUseCase,
    private val unlockRoutesUseCase: UnlockRoutesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    private var messageJob: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        val profile = trailRepository.profile()
        val tokens = profile.lifetimeTokens
        val routes = SampleData.routes.map { route ->
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
        val storedSelection = trailRepository.selectedRouteId()
        val selected = routes.firstOrNull { it.id == storedSelection && !it.locked }?.id
            ?: routes.first { !it.locked }.id
        val daily = dailyChallengeUseCase.resolve(
            trailRepository.storedDaily(),
            dailyChallengeUseCase.todayKey(),
            SampleData.routeIds
        )
        val claimable = SampleData.rewardTiers.count {
            tokens >= it.requirement && it.id !in profile.claimedTiers
        }
        _state.update {
            it.copy(
                tokens = tokens,
                routes = routes,
                selectedRouteId = selected,
                bestSteps = profile.bestSteps.values.maxOrNull() ?: 0,
                dailyDone = daily.claimed,
                claimableRewards = claimable
            )
        }
    }

    fun selectRoute(routeId: String) {
        val route = _state.value.routes.firstOrNull { it.id == routeId } ?: return
        if (route.locked) {
            showMessage(R.string.route_locked, route.unlockTokens)
            return
        }
        trailRepository.selectRoute(routeId)
        _state.update { it.copy(selectedRouteId = routeId, message = null) }
    }

    private fun showMessage(messageRes: Int, arg: Int) {
        messageJob?.cancel()
        _state.update { it.copy(message = messageRes, messageArg = arg) }
        messageJob = viewModelScope.launch {
            delay(GameConfig.ROUTE_MESSAGE_MS)
            _state.update { it.copy(message = null) }
        }
    }
}
