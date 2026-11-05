package com.Dopxafi4.Wewwem.presentation.daily

import androidx.lifecycle.ViewModel
import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import com.Dopxafi4.Wewwem.domain.usecase.DailyChallengeUseCase
import com.Dopxafi4.Wewwem.presentation.common.Labels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DailyChallengeViewModel(
    private val trailRepository: TrailRepository,
    private val dailyChallengeUseCase: DailyChallengeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyChallengeUiState())
    val state: StateFlow<DailyChallengeUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val challenge = current()
        trailRepository.saveDaily(challenge)
        val profile = trailRepository.profile()
        _state.value = DailyChallengeUiState(
            goal = challenge.goal,
            target = challenge.target,
            progress = challenge.progress,
            fraction = challenge.fraction,
            routeTitleRes = Labels.route(challenge.routeId),
            completed = challenge.completed,
            claimed = challenge.claimed,
            shields = profile.shields
        )
    }

    fun startChallenge() {
        trailRepository.selectRoute(current().routeId)
    }

    fun claimReward() {
        val challenge = current()
        if (!challenge.completed || challenge.claimed) return
        val profile = trailRepository.profile()
        trailRepository.saveProfile(
            profile.copy(
                lifetimeTokens = profile.lifetimeTokens + GameConfig.DAILY_TOKEN_REWARD,
                shields = (profile.shields + 1).coerceAtMost(GameConfig.MAX_SHIELDS)
            )
        )
        trailRepository.saveDaily(challenge.copy(claimed = true))
        refresh()
    }

    private fun current() = dailyChallengeUseCase.resolve(
        trailRepository.storedDaily(),
        dailyChallengeUseCase.todayKey(),
        SampleData.routeIds
    )
}
