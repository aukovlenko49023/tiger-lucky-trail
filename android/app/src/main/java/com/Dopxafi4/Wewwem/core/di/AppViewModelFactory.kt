package com.Dopxafi4.Wewwem.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.Dopxafi4.Wewwem.presentation.app.AppViewModel
import com.Dopxafi4.Wewwem.presentation.daily.DailyChallengeViewModel
import com.Dopxafi4.Wewwem.presentation.game.GameViewModel
import com.Dopxafi4.Wewwem.presentation.gameover.GameOverViewModel
import com.Dopxafi4.Wewwem.presentation.menu.MenuViewModel
import com.Dopxafi4.Wewwem.presentation.progress.ProgressViewModel
import com.Dopxafi4.Wewwem.presentation.rewards.RewardsViewModel
import com.Dopxafi4.Wewwem.presentation.settings.SettingsViewModel
import com.Dopxafi4.Wewwem.presentation.splash.SplashViewModel

object AppViewModelFactory {

    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { AppViewModel() }
        initializer { SplashViewModel() }
        initializer {
            MenuViewModel(
                ServiceLocator.trailRepository(),
                ServiceLocator.dailyChallenge,
                ServiceLocator.unlockRoutes
            )
        }
        initializer {
            GameViewModel(
                ServiceLocator.trailRepository(),
                ServiceLocator.settingsRepository(),
                ServiceLocator.buildTrail,
                ServiceLocator.resolveStep,
                ServiceLocator.bankRun,
                ServiceLocator.dailyChallenge
            )
        }
        initializer { GameOverViewModel(ServiceLocator.trailRepository()) }
        initializer {
            DailyChallengeViewModel(
                ServiceLocator.trailRepository(),
                ServiceLocator.dailyChallenge
            )
        }
        initializer {
            ProgressViewModel(
                ServiceLocator.trailRepository(),
                ServiceLocator.unlockRoutes
            )
        }
        initializer { RewardsViewModel(ServiceLocator.trailRepository()) }
        initializer {
            SettingsViewModel(
                ServiceLocator.trailRepository(),
                ServiceLocator.settingsRepository()
            )
        }
    }
}
