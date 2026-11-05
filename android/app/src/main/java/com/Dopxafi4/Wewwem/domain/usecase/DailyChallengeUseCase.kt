package com.Dopxafi4.Wewwem.domain.usecase

import com.Dopxafi4.Wewwem.domain.model.DailyChallenge
import com.Dopxafi4.Wewwem.domain.model.DailyGoal
import com.Dopxafi4.Wewwem.domain.model.RunResult
import java.time.LocalDate
import kotlin.random.Random

class DailyChallengeUseCase {

    fun todayKey(): Long = LocalDate.now().toEpochDay()

    fun build(dayKey: Long, routeIds: List<String>): DailyChallenge {
        val random = Random(dayKey)
        val goal = DailyGoal.entries[random.nextInt(DailyGoal.entries.size)]
        val routeId = routeIds[random.nextInt(routeIds.size)]
        val target = when (goal) {
            DailyGoal.REACH_STEP -> 4 + random.nextInt(4)
            DailyGoal.BANK_TOKENS -> 120 + random.nextInt(8) * 20
            DailyGoal.COMPLETE_RUNS -> 2 + random.nextInt(3)
        }
        return DailyChallenge(
            dayKey = dayKey,
            goal = goal,
            target = target,
            routeId = routeId,
            progress = 0,
            claimed = false
        )
    }

    fun resolve(stored: DailyChallenge?, dayKey: Long, routeIds: List<String>): DailyChallenge =
        if (stored != null && stored.dayKey == dayKey) stored else build(dayKey, routeIds)

    fun advance(challenge: DailyChallenge, result: RunResult): DailyChallenge {
        val gained = when (challenge.goal) {
            DailyGoal.REACH_STEP -> if (result.steps > challenge.progress) {
                result.steps - challenge.progress
            } else {
                0
            }
            DailyGoal.BANK_TOKENS -> if (result.banked) result.tokens else 0
            DailyGoal.COMPLETE_RUNS -> 1
        }
        if (gained <= 0) return challenge
        return challenge.copy(progress = (challenge.progress + gained).coerceAtMost(challenge.target))
    }
}
