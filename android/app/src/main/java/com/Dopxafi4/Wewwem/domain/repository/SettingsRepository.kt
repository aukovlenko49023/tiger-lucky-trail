package com.Dopxafi4.Wewwem.domain.repository

interface SettingsRepository {
    fun haptics(): Boolean
    fun setHaptics(value: Boolean)
    fun reducedMotion(): Boolean
    fun setReducedMotion(value: Boolean)
    fun stoneHints(): Boolean
    fun setStoneHints(value: Boolean)
}
