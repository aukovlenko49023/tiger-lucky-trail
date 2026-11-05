package com.Dopxafi4.Wewwem.presentation.rewards

import androidx.annotation.StringRes

enum class RewardsTab {
    REWARDS,
    COLLECTION
}

data class RewardTierUi(
    val id: String,
    @StringRes val titleRes: Int,
    val requirement: Int,
    val claimed: Boolean,
    val reached: Boolean,
    val remaining: Int
)

data class CharmUi(
    val id: String,
    @StringRes val titleRes: Int,
    val unlocked: Boolean
)

data class RewardsUiState(
    val tab: RewardsTab = RewardsTab.REWARDS,
    val tokens: Int = 0,
    val shields: Int = 0,
    val tiers: List<RewardTierUi> = emptyList(),
    val charms: List<CharmUi> = emptyList()
)
