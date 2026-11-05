package com.Dopxafi4.Wewwem.domain.model

enum class DailyGoal {
    REACH_STEP,
    BANK_TOKENS,
    COMPLETE_RUNS
}

data class DailyChallenge(
    val dayKey: Long,
    val goal: DailyGoal,
    val target: Int,
    val routeId: String,
    val progress: Int,
    val claimed: Boolean
) {
    val completed: Boolean get() = progress >= target
    val fraction: Float
        get() = if (target <= 0) 0f else (progress.toFloat() / target.toFloat()).coerceIn(0f, 1f)
}
