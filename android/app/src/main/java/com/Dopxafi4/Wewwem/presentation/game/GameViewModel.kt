package com.Dopxafi4.Wewwem.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.domain.model.Route
import com.Dopxafi4.Wewwem.domain.model.RunResult
import com.Dopxafi4.Wewwem.domain.model.StoneState
import com.Dopxafi4.Wewwem.domain.model.TrailRow
import com.Dopxafi4.Wewwem.domain.repository.SettingsRepository
import com.Dopxafi4.Wewwem.domain.repository.TrailRepository
import com.Dopxafi4.Wewwem.domain.usecase.BankRunUseCase
import com.Dopxafi4.Wewwem.domain.usecase.BuildTrailUseCase
import com.Dopxafi4.Wewwem.domain.usecase.DailyChallengeUseCase
import com.Dopxafi4.Wewwem.domain.usecase.ResolveStepUseCase
import com.Dopxafi4.Wewwem.presentation.common.Labels
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val trailRepository: TrailRepository,
    private val settingsRepository: SettingsRepository,
    private val buildTrailUseCase: BuildTrailUseCase,
    private val resolveStepUseCase: ResolveStepUseCase,
    private val bankRunUseCase: BankRunUseCase,
    private val dailyChallengeUseCase: DailyChallengeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var activeRunId = 0L
    private var job: Job? = null
    private var messageJob: Job? = null
    private var route: Route = SampleData.routes.first()
    private var hintsEnabled = false

    fun startRun(runId: Long) {
        if (activeRunId == runId) return
        activeRunId = runId
        job?.cancel()
        messageJob?.cancel()
        route = SampleData.routeById(trailRepository.selectedRouteId())
        hintsEnabled = settingsRepository.stoneHints()
        val profile = trailRepository.profile()
        val rows = buildTrailUseCase(route)
        _state.value = GameUiState(
            routeId = route.id,
            routeTitleRes = Labels.route(route.id),
            rows = rows,
            rowRewards = List(route.rows) { route.stepReward(it + 1) },
            totalRows = route.rows,
            tiles = route.tiles,
            activeRow = 0,
            tigerRow = -1,
            tigerColumn = route.tiles / 2,
            runTokens = 0,
            bankedTotal = profile.lifetimeTokens,
            shields = profile.shields,
            step = 0,
            nextReward = route.stepReward(1),
            phase = GamePhase.READY,
            hintRow = if (hintsEnabled) 0 else -1,
            hintColumn = if (hintsEnabled) rows.first().firstSafeIndex() else -1,
            reducedMotion = settingsRepository.reducedMotion(),
            haptics = settingsRepository.haptics()
        )
    }

    fun onStoneTap(column: Int) {
        val current = _state.value
        if (!current.inputEnabled) return
        val rowIndex = current.activeRow
        if (rowIndex !in current.rows.indices) return
        job?.cancel()
        job = viewModelScope.launch {
            _state.update {
                it.copy(
                    phase = GamePhase.RESOLVING,
                    tigerRow = rowIndex,
                    tigerColumn = column,
                    leapTick = it.leapTick + 1,
                    message = null
                )
            }
            delay(GameConfig.REVEAL_DELAY_MS)
            val snapshot = _state.value
            val outcome = resolveStepUseCase(
                row = snapshot.rows[rowIndex],
                column = column,
                route = route,
                step = snapshot.step + 1,
                shields = snapshot.shields
            )
            when {
                outcome.shieldUsed -> applyShield(rowIndex, column)
                outcome.safe -> applySafe(rowIndex, column, outcome.reward)
                else -> applyHazard(rowIndex, column)
            }
        }
    }

    fun bankTokens() {
        if (!_state.value.canBank) return
        job?.cancel()
        job = viewModelScope.launch {
            finish(banked = true, bonus = 0)
        }
    }

    fun pause() {
        job?.cancel()
        messageJob?.cancel()
    }

    private suspend fun applySafe(rowIndex: Int, column: Int, reward: Int) {
        val resolved = closeRow(_state.value.rows, rowIndex, column, StoneState.SAFE)
        val nextRow = rowIndex + 1
        val finished = nextRow >= route.rows
        _state.update {
            it.copy(
                rows = resolved,
                runTokens = it.runTokens + reward,
                step = it.step + 1,
                activeRow = nextRow,
                nextReward = if (finished) 0 else route.stepReward(nextRow + 1),
                phase = if (finished) GamePhase.RESOLVING else GamePhase.CHOOSING,
                gainTick = it.gainTick + 1,
                hintRow = if (hintsEnabled && !finished && nextRow < GameConfig.HINT_STEPS) {
                    nextRow
                } else {
                    -1
                },
                hintColumn = if (hintsEnabled && !finished && nextRow < GameConfig.HINT_STEPS) {
                    resolved[nextRow].firstSafeIndex()
                } else {
                    -1
                }
            )
        }
        if (finished) {
            finish(banked = true, bonus = route.summitBonus)
        }
    }

    private fun applyShield(rowIndex: Int, column: Int) {
        val resolved = closeRow(_state.value.rows, rowIndex, column, StoneState.SHIELDED)
        val nextRow = rowIndex + 1
        _state.update {
            it.copy(
                rows = resolved,
                shields = (it.shields - 1).coerceAtLeast(0),
                step = it.step + 1,
                activeRow = nextRow,
                nextReward = if (nextRow >= route.rows) 0 else route.stepReward(nextRow + 1),
                phase = GamePhase.CHOOSING,
                message = R.string.shield_absorbed,
                hintRow = -1,
                hintColumn = -1
            )
        }
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            delay(GameConfig.SHIELD_MESSAGE_MS)
            _state.update { it.copy(message = null) }
        }
    }

    private suspend fun applyHazard(rowIndex: Int, column: Int) {
        val revealed = _state.value.rows.mapIndexed { index, row ->
            if (index != rowIndex) {
                row
            } else {
                row.copy(
                    stones = row.stones.mapIndexed { stoneIndex, _ ->
                        when {
                            stoneIndex == column -> StoneState.HAZARD
                            stoneIndex in row.hazards -> StoneState.HAZARD
                            else -> StoneState.PASSED
                        }
                    }
                )
            }
        }
        _state.update {
            it.copy(
                rows = revealed,
                phase = GamePhase.BLOCKED,
                shakeTick = it.shakeTick + 1,
                hintRow = -1,
                hintColumn = -1
            )
        }
        delay(GameConfig.GAME_OVER_DELAY_MS)
        finish(banked = false, bonus = 0)
    }

    private suspend fun finish(banked: Boolean, bonus: Int) {
        val snapshot = _state.value
        val profile = trailRepository.profile()
        val result = RunResult(
            routeId = route.id,
            banked = banked,
            tokens = snapshot.runTokens + if (banked) bonus else 0,
            steps = snapshot.step,
            totalSteps = route.rows,
            previousBest = profile.bestOn(route.id)
        )
        trailRepository.saveProfile(bankRunUseCase(profile, result, snapshot.shields))
        trailRepository.saveRunResult(result)
        val today = dailyChallengeUseCase.todayKey()
        val daily = dailyChallengeUseCase.resolve(
            trailRepository.storedDaily(),
            today,
            SampleData.routeIds
        )
        trailRepository.saveDaily(dailyChallengeUseCase.advance(daily, result))
        _state.update { it.copy(phase = GamePhase.FINISHED) }
        _events.send(GameEvent.Finished)
    }

    private fun closeRow(
        rows: List<TrailRow>,
        rowIndex: Int,
        column: Int,
        chosen: StoneState
    ): List<TrailRow> = rows.mapIndexed { index, row ->
        if (index != rowIndex) {
            row
        } else {
            row.copy(
                stones = row.stones.mapIndexed { stoneIndex, _ ->
                    if (stoneIndex == column) chosen else StoneState.PASSED
                }
            )
        }
    }
}
