package com.Dopxafi4.Wewwem.domain.usecase

import com.Dopxafi4.Wewwem.domain.model.Route
import com.Dopxafi4.Wewwem.domain.model.TrailRow

data class StepOutcome(
    val safe: Boolean,
    val shieldUsed: Boolean,
    val reward: Int
)

class ResolveStepUseCase {

    operator fun invoke(
        row: TrailRow,
        column: Int,
        route: Route,
        step: Int,
        shields: Int
    ): StepOutcome {
        val hit = column in row.hazards
        if (!hit) {
            return StepOutcome(safe = true, shieldUsed = false, reward = route.stepReward(step))
        }
        if (shields > 0) {
            return StepOutcome(safe = true, shieldUsed = true, reward = 0)
        }
        return StepOutcome(safe = false, shieldUsed = false, reward = 0)
    }
}
