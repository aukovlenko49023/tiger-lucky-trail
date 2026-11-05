package com.Dopxafi4.Wewwem.domain.usecase

import com.Dopxafi4.Wewwem.domain.model.PlayerProfile
import com.Dopxafi4.Wewwem.domain.model.RunResult

class BankRunUseCase {

    operator fun invoke(profile: PlayerProfile, result: RunResult, shieldsLeft: Int): PlayerProfile {
        val bests = profile.bestSteps.toMutableMap()
        if (result.steps > profile.bestOn(result.routeId)) {
            bests[result.routeId] = result.steps
        }
        return profile.copy(
            lifetimeTokens = profile.lifetimeTokens + if (result.banked) result.tokens else 0,
            totalRuns = profile.totalRuns + 1,
            shields = shieldsLeft,
            bestSteps = bests.toMap()
        )
    }
}
