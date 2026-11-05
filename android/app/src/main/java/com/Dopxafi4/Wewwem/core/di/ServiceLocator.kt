package com.Dopxafi4.Wewwem.core.di

import android.content.Context
import com.Dopxafi4.Wewwem.data.local.TigerPreferences
import com.Dopxafi4.Wewwem.data.repository.SettingsRepositoryImpl
import com.Dopxafi4.Wewwem.data.repository.TrailRepositoryImpl
import com.Dopxafi4.Wewwem.domain.repository.SettingsRepository
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import com.Dopxafi4.Wewwem.domain.usecase.BankRunUseCase
import com.Dopxafi4.Wewwem.domain.usecase.BuildTrailUseCase
import com.Dopxafi4.Wewwem.domain.usecase.DailyChallengeUseCase
import com.Dopxafi4.Wewwem.domain.usecase.ResolveStepUseCase
import com.Dopxafi4.Wewwem.domain.usecase.UnlockRoutesUseCase

object ServiceLocator {

    private var trail: TrailRepository? = null
    private var settings: SettingsRepository? = null

    val buildTrail = BuildTrailUseCase()
    val resolveStep = ResolveStepUseCase()
    val bankRun = BankRunUseCase()
    val unlockRoutes = UnlockRoutesUseCase()
    val dailyChallenge = DailyChallengeUseCase()

    fun init(context: Context) {
        if (trail != null) return
        val storage = TigerPreferences(context.applicationContext)
        trail = TrailRepositoryImpl(storage)
        settings = SettingsRepositoryImpl(storage)
    }

    fun trailRepository(): TrailRepository = checkNotNull(trail)

    fun settingsRepository(): SettingsRepository = checkNotNull(settings)
}
