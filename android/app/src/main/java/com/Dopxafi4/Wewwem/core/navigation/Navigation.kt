package com.Dopxafi4.Wewwem.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val runId: Long) : Screen
    data class GameOver(val runId: Long) : Screen
    data object Daily : Screen
    data object Progress : Screen
    data object Rewards : Screen
    data object Settings : Screen
}
