package com.Dopxafi4.Wewwem.presentation.app

import androidx.lifecycle.ViewModel
import com.Dopxafi4.Wewwem.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val backStack = mutableListOf<Screen>()
    private var runCounter = 0L

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) {
            backStack.add(current)
        }
        _screen.value = target
        _canGoBack.value = backStack.isNotEmpty()
    }

    fun replaceTop(target: Screen) {
        _screen.value = target
        _canGoBack.value = backStack.isNotEmpty()
    }

    fun resetTo(target: Screen) {
        backStack.clear()
        _screen.value = target
        _canGoBack.value = false
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        _canGoBack.value = backStack.isNotEmpty()
    }

    fun nextRunId(): Long {
        runCounter += 1
        return runCounter
    }
}
