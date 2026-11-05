package com.Dopxafi4.Wewwem.domain.repository

import com.Dopxafi4.Wewwem.domain.model.DailyChallenge
import com.Dopxafi4.Wewwem.domain.model.PlayerProfile
import com.Dopxafi4.Wewwem.domain.model.RunResult

interface TrailRepository {
    fun profile(): PlayerProfile
    fun saveProfile(profile: PlayerProfile)
    fun selectedRouteId(): String
    fun selectRoute(routeId: String)
    fun saveRunResult(result: RunResult)
    fun lastRunResult(): RunResult?
    fun storedDaily(): DailyChallenge?
    fun saveDaily(challenge: DailyChallenge)
    fun resetProgress()
}
