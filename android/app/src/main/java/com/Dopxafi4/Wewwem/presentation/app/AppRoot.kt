package com.Dopxafi4.Wewwem.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.navigation.Screen
import com.Dopxafi4.Wewwem.presentation.daily.DailyChallengeScreen
import com.Dopxafi4.Wewwem.presentation.game.GameScreen
import com.Dopxafi4.Wewwem.presentation.gameover.GameOverScreen
import com.Dopxafi4.Wewwem.presentation.menu.MenuScreen
import com.Dopxafi4.Wewwem.presentation.progress.ProgressScreen
import com.Dopxafi4.Wewwem.presentation.rewards.RewardsScreen
import com.Dopxafi4.Wewwem.presentation.settings.SettingsScreen
import com.Dopxafi4.Wewwem.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn(tween(GameConfig.SCREEN_FADE_MS)) togetherWith fadeOut(tween(240))
        },
        modifier = Modifier.fillMaxSize()
    ) { target ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (target) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.Menu -> MenuScreen(
                    onPlay = { appViewModel.navigate(Screen.Game(appViewModel.nextRunId())) },
                    onDaily = { appViewModel.navigate(Screen.Daily) },
                    onProgress = { appViewModel.navigate(Screen.Progress) },
                    onRewards = { appViewModel.navigate(Screen.Rewards) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                is Screen.Game -> GameScreen(
                    runId = target.runId,
                    onFinished = {
                        appViewModel.replaceTop(Screen.GameOver(appViewModel.nextRunId()))
                    },
                    onExit = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.GameOver -> GameOverScreen(
                    runId = target.runId,
                    onPlayAgain = {
                        appViewModel.replaceTop(Screen.Game(appViewModel.nextRunId()))
                    },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.Daily -> DailyChallengeScreen(
                    onStart = { appViewModel.navigate(Screen.Game(appViewModel.nextRunId())) },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.Progress -> ProgressScreen(
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.Rewards -> RewardsScreen(
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.Settings -> SettingsScreen(
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )
            }
        }
    }
}
