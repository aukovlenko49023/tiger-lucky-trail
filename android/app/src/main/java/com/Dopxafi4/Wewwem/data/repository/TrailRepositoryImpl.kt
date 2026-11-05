package com.Dopxafi4.Wewwem.data.repository

import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.data.local.TigerPreferences
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.domain.model.DailyChallenge
import com.Dopxafi4.Wewwem.domain.model.DailyGoal
import com.Dopxafi4.Wewwem.domain.model.PlayerProfile
import com.Dopxafi4.Wewwem.domain.model.RunResult
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository

class TrailRepositoryImpl(private val storage: TigerPreferences) : TrailRepository {

    override fun profile(): PlayerProfile {
        if (!storage.boolean(TigerPreferences.KEY_PROFILE_READY, false)) {
            storage.putBoolean(TigerPreferences.KEY_PROFILE_READY, true)
            storage.putInt(TigerPreferences.KEY_LIFETIME_TOKENS, GameConfig.STARTING_TOKENS)
        }
        val bests = SampleData.routeIds.associateWith {
            storage.int(TigerPreferences.KEY_BEST_PREFIX + it, 0)
        }
        return PlayerProfile(
            lifetimeTokens = storage.int(TigerPreferences.KEY_LIFETIME_TOKENS, GameConfig.STARTING_TOKENS),
            totalRuns = storage.int(TigerPreferences.KEY_TOTAL_RUNS, 0),
            shields = storage.int(TigerPreferences.KEY_SHIELDS, 0),
            bestSteps = bests,
            claimedTiers = storage.stringSet(TigerPreferences.KEY_CLAIMED_TIERS).toSet(),
            unlockedCharms = storage.stringSet(TigerPreferences.KEY_UNLOCKED_CHARMS).toSet()
        )
    }

    override fun saveProfile(profile: PlayerProfile) {
        storage.putBoolean(TigerPreferences.KEY_PROFILE_READY, true)
        storage.putInt(TigerPreferences.KEY_LIFETIME_TOKENS, profile.lifetimeTokens)
        storage.putInt(TigerPreferences.KEY_TOTAL_RUNS, profile.totalRuns)
        storage.putInt(TigerPreferences.KEY_SHIELDS, profile.shields.coerceIn(0, GameConfig.MAX_SHIELDS))
        profile.bestSteps.forEach { (routeId, best) ->
            storage.putInt(TigerPreferences.KEY_BEST_PREFIX + routeId, best)
        }
        storage.putStringSet(TigerPreferences.KEY_CLAIMED_TIERS, profile.claimedTiers.toSet())
        storage.putStringSet(TigerPreferences.KEY_UNLOCKED_CHARMS, profile.unlockedCharms.toSet())
    }

    override fun selectedRouteId(): String =
        storage.string(TigerPreferences.KEY_SELECTED_ROUTE, SampleData.ROUTE_BAMBOO)

    override fun selectRoute(routeId: String) {
        storage.putString(TigerPreferences.KEY_SELECTED_ROUTE, routeId)
    }

    override fun saveRunResult(result: RunResult) {
        storage.putBoolean(TigerPreferences.KEY_LAST_PRESENT, true)
        storage.putString(TigerPreferences.KEY_LAST_ROUTE, result.routeId)
        storage.putBoolean(TigerPreferences.KEY_LAST_BANKED, result.banked)
        storage.putInt(TigerPreferences.KEY_LAST_TOKENS, result.tokens)
        storage.putInt(TigerPreferences.KEY_LAST_STEPS, result.steps)
        storage.putInt(TigerPreferences.KEY_LAST_TOTAL, result.totalSteps)
        storage.putInt(TigerPreferences.KEY_LAST_BEST, result.previousBest)
    }

    override fun lastRunResult(): RunResult? {
        if (!storage.boolean(TigerPreferences.KEY_LAST_PRESENT, false)) return null
        return RunResult(
            routeId = storage.string(TigerPreferences.KEY_LAST_ROUTE, SampleData.ROUTE_BAMBOO),
            banked = storage.boolean(TigerPreferences.KEY_LAST_BANKED, false),
            tokens = storage.int(TigerPreferences.KEY_LAST_TOKENS, 0),
            steps = storage.int(TigerPreferences.KEY_LAST_STEPS, 0),
            totalSteps = storage.int(TigerPreferences.KEY_LAST_TOTAL, 0),
            previousBest = storage.int(TigerPreferences.KEY_LAST_BEST, 0)
        )
    }

    override fun storedDaily(): DailyChallenge? {
        val day = storage.long(TigerPreferences.KEY_DAILY_DATE, 0L)
        if (day == 0L) return null
        val goalName = storage.string(TigerPreferences.KEY_DAILY_GOAL, DailyGoal.REACH_STEP.name)
        val goal = DailyGoal.entries.firstOrNull { it.name == goalName } ?: DailyGoal.REACH_STEP
        return DailyChallenge(
            dayKey = day,
            goal = goal,
            target = storage.int(TigerPreferences.KEY_DAILY_TARGET, 5),
            routeId = storage.string(TigerPreferences.KEY_DAILY_ROUTE, SampleData.ROUTE_BAMBOO),
            progress = storage.int(TigerPreferences.KEY_DAILY_PROGRESS, 0),
            claimed = storage.boolean(TigerPreferences.KEY_DAILY_CLAIMED, false)
        )
    }

    override fun saveDaily(challenge: DailyChallenge) {
        storage.putLong(TigerPreferences.KEY_DAILY_DATE, challenge.dayKey)
        storage.putString(TigerPreferences.KEY_DAILY_GOAL, challenge.goal.name)
        storage.putInt(TigerPreferences.KEY_DAILY_TARGET, challenge.target)
        storage.putString(TigerPreferences.KEY_DAILY_ROUTE, challenge.routeId)
        storage.putInt(TigerPreferences.KEY_DAILY_PROGRESS, challenge.progress)
        storage.putBoolean(TigerPreferences.KEY_DAILY_CLAIMED, challenge.claimed)
    }

    override fun resetProgress() {
        storage.clearAll()
        storage.putBoolean(TigerPreferences.KEY_PROFILE_READY, true)
        storage.putInt(TigerPreferences.KEY_LIFETIME_TOKENS, GameConfig.STARTING_TOKENS)
    }
}
