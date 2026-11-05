package com.Dopxafi4.Wewwem.presentation.settings

data class SettingsUiState(
    val haptics: Boolean = true,
    val reducedMotion: Boolean = false,
    val stoneHints: Boolean = true,
    val resetDone: Boolean = false
)
