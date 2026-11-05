package com.Dopxafi4.Wewwem.data.repository

import com.Dopxafi4.Wewwem.data.local.TigerPreferences
import com.Dopxafi4.Wewwem.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val storage: TigerPreferences) : SettingsRepository {

    override fun haptics(): Boolean = storage.boolean(TigerPreferences.KEY_HAPTICS, true)

    override fun setHaptics(value: Boolean) {
        storage.putBoolean(TigerPreferences.KEY_HAPTICS, value)
    }

    override fun reducedMotion(): Boolean =
        storage.boolean(TigerPreferences.KEY_REDUCED_MOTION, false)

    override fun setReducedMotion(value: Boolean) {
        storage.putBoolean(TigerPreferences.KEY_REDUCED_MOTION, value)
    }

    override fun stoneHints(): Boolean = storage.boolean(TigerPreferences.KEY_HINTS, true)

    override fun setStoneHints(value: Boolean) {
        storage.putBoolean(TigerPreferences.KEY_HINTS, value)
    }
}
