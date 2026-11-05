package com.Dopxafi4.Wewwem.presentation.settings

import androidx.lifecycle.ViewModel
import com.Dopxafi4.Wewwem.domain.repository.SettingsRepository
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val trailRepository: TrailRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update {
            it.copy(
                haptics = settingsRepository.haptics(),
                reducedMotion = settingsRepository.reducedMotion(),
                stoneHints = settingsRepository.stoneHints()
            )
        }
    }

    fun setHaptics(value: Boolean) {
        settingsRepository.setHaptics(value)
        _state.update { it.copy(haptics = value, resetDone = false) }
    }

    fun setReducedMotion(value: Boolean) {
        settingsRepository.setReducedMotion(value)
        _state.update { it.copy(reducedMotion = value, resetDone = false) }
    }

    fun setStoneHints(value: Boolean) {
        settingsRepository.setStoneHints(value)
        _state.update { it.copy(stoneHints = value, resetDone = false) }
    }

    fun resetProgress() {
        trailRepository.resetProgress()
        settingsRepository.setHaptics(_state.value.haptics)
        settingsRepository.setReducedMotion(_state.value.reducedMotion)
        settingsRepository.setStoneHints(_state.value.stoneHints)
        _state.update { it.copy(resetDone = true) }
    }
}
