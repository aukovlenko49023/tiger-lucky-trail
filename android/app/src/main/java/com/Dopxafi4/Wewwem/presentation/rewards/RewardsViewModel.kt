package com.Dopxafi4.Wewwem.presentation.rewards

import androidx.lifecycle.ViewModel
import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import com.Dopxafi4.Wewwem.presentation.common.Labels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RewardsViewModel(
    private val trailRepository: TrailRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val profile = trailRepository.profile()
        val tokens = profile.lifetimeTokens
        _state.update { current ->
            current.copy(
                tokens = tokens,
                shields = profile.shields,
                tiers = SampleData.rewardTiers.map { tier ->
                    RewardTierUi(
                        id = tier.id,
                        titleRes = Labels.tier(tier.id),
                        requirement = tier.requirement,
                        claimed = tier.id in profile.claimedTiers,
                        reached = tokens >= tier.requirement,
                        remaining = (tier.requirement - tokens).coerceAtLeast(0)
                    )
                },
                charms = SampleData.charms.map { charm ->
                    CharmUi(
                        id = charm.id,
                        titleRes = Labels.charm(charm.id),
                        unlocked = charm.id in profile.unlockedCharms
                    )
                }
            )
        }
    }

    fun selectTab(tab: RewardsTab) {
        _state.update { it.copy(tab = tab) }
    }

    fun claimTier(tierId: String) {
        val profile = trailRepository.profile()
        val tier = SampleData.rewardTiers.firstOrNull { it.id == tierId } ?: return
        if (profile.lifetimeTokens < tier.requirement) return
        if (tier.id in profile.claimedTiers) return
        trailRepository.saveProfile(
            profile.copy(
                shields = (profile.shields + 1).coerceAtMost(GameConfig.MAX_SHIELDS),
                claimedTiers = profile.claimedTiers + tier.id,
                unlockedCharms = profile.unlockedCharms + tier.charmId
            )
        )
        refresh()
    }
}
