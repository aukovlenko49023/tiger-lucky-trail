package com.Dopxafi4.Wewwem.presentation.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.theme.Cream
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleDeep
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.LeafCreamDeep
import com.Dopxafi4.Wewwem.core.theme.Overlays
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.SurfacePlank
import com.Dopxafi4.Wewwem.core.theme.TigerOrange
import com.Dopxafi4.Wewwem.core.theme.TigerOrangeDeep
import com.Dopxafi4.Wewwem.core.ui.AppBackground
import com.Dopxafi4.Wewwem.core.ui.GlyphIconButton
import com.Dopxafi4.Wewwem.core.ui.PrimaryButton
import com.Dopxafi4.Wewwem.domain.formatTokens
import com.Dopxafi4.Wewwem.presentation.dialog.HowToDialog
import com.Dopxafi4.Wewwem.presentation.dialog.PauseDialog
import com.Dopxafi4.Wewwem.presentation.game.components.TrailStone
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun GameScreen(
    runId: Long,
    onFinished: () -> Unit,
    onExit: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    val haptics = LocalHapticFeedback.current
    var showPause by rememberSaveable { mutableStateOf(false) }
    var showHowTo by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(runId) {
        gameViewModel.startRun(runId)
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onStoneTap = { column ->
            if (state.haptics) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            gameViewModel.onStoneTap(column)
        },
        onBank = {
            if (state.haptics) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            gameViewModel.bankTokens()
        },
        onHome = {
            if (state.step == 0) {
                onExit()
            } else {
                showPause = true
            }
        },
        onPause = { showPause = true },
        onHowTo = { showHowTo = true }
    )

    if (showPause) {
        PauseDialog(
            onResume = { showPause = false },
            onMenu = {
                showPause = false
                onExit()
            }
        )
    }

    if (showHowTo) {
        HowToDialog(onDismiss = { showHowTo = false })
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onStoneTap: (Int) -> Unit,
    onBank: () -> Unit,
    onHome: () -> Unit,
    onPause: () -> Unit,
    onHowTo: () -> Unit
) {
    val transition = rememberInfiniteTransition()
    val pulse by transition.animateFloat(
        initialValue = 0.38f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val glow = if (state.reducedMotion) 0.85f else pulse
    val shake by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.BLOCKED && !state.reducedMotion) 1f else 0f,
        animationSpec = tween(durationMillis = 280, easing = LinearEasing)
    )
    val displayedTokens by animateIntAsState(
        targetValue = state.runTokens,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
    )

    AppBackground(overlay = Overlays.game) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                state = state,
                onHome = onHome,
                onPause = onPause
            )

            RunBanner(
                tokens = displayedTokens,
                shields = state.shields,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                TrailBoard(
                    state = state,
                    glow = glow,
                    shake = shake,
                    onStoneTap = onStoneTap,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, bottom = 196.dp)
                )

                FloatingControls(
                    state = state,
                    onBank = onBank,
                    onHowTo = onHowTo,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 14.dp)
                )
            }
        }
    }
}

@Composable
private fun GameHeader(
    state: GameUiState,
    onHome: () -> Unit,
    onPause: () -> Unit
) {
    Surface(
        color = InkForest.copy(alpha = 0.45f),
        shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlyphIconButton(
                image = R.drawable.icon_home,
                contentDescription = stringResource(R.string.cd_menu),
                onClick = onHome
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(state.routeTitleRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = Cream,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.step_counter, state.step, state.totalRows),
                    style = MaterialTheme.typography.labelSmall,
                    color = LeafCreamDeep,
                    textAlign = TextAlign.Center
                )
            }
            GlyphIconButton(
                image = R.drawable.icon_pause,
                contentDescription = stringResource(R.string.cd_pause),
                onClick = onPause
            )
        }
    }
}

@Composable
private fun RunBanner(
    tokens: Int,
    shields: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.run_tokens),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss
                )
                Text(
                    text = formatTokens(tokens),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 22.sp,
                    color = TigerOrangeDeep
                )
            }
        }
        if (shields > 0) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = JungleGreen.copy(alpha = 0.18f),
                border = BorderStroke(1.5.dp, JungleGreen),
                modifier = Modifier.height(30.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_shield),
                        contentDescription = null,
                        tint = JungleGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.shield_count, shields),
                        style = MaterialTheme.typography.labelSmall,
                        color = JungleDeep
                    )
                }
            }
        }
    }
}

@Composable
private fun TrailBoard(
    state: GameUiState,
    glow: Float,
    shake: Float,
    onStoneTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = 10.dp
    val ground = 42.dp
    BoxWithConstraints(
        modifier = modifier
            .clipToBounds()
            .graphicsLayer {
                translationX = sin(shake * PI.toFloat() * 6f) * 6.dp.toPx()
            }
    ) {
        val visible = GameConfig.VISIBLE_ROWS
        val rowHeight = ((maxHeight - ground - spacing * (visible + 1)) / visible)
            .coerceIn(46.dp, 82.dp)
        val maxFirst = (state.totalRows - visible).coerceAtLeast(0)
        val firstVisible = (state.activeRow - 1).coerceIn(0, maxFirst)
        val scrollOffset by animateDpAsState(
            targetValue = (rowHeight + spacing) * firstVisible,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = scrollOffset),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            for (index in state.rows.indices.reversed()) {
                val row = state.rows[index]
                val isActive = index == state.activeRow && state.inputEnabled
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(rowHeight),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    row.stones.forEachIndexed { column, stone ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            TrailStone(
                                state = stone,
                                reward = state.rowRewards.getOrElse(index) { 0 },
                                active = isActive,
                                showHint = state.hintRow == index && state.hintColumn == column,
                                enabled = state.inputEnabled,
                                glow = glow,
                                onClick = { onStoneTap(column) },
                                modifier = Modifier.fillMaxSize()
                            )
                            if (state.tigerRow == index && state.tigerColumn == column) {
                                TigerPawn(
                                    reducedMotion = state.reducedMotion,
                                    resolving = state.phase == GamePhase.RESOLVING,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ground),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Sage.copy(alpha = 0.45f))
                )
                if (state.tigerRow < 0) {
                    TigerPawn(
                        reducedMotion = state.reducedMotion,
                        resolving = state.phase == GamePhase.RESOLVING
                    )
                }
            }
        }
    }
}

@Composable
private fun TigerPawn(
    reducedMotion: Boolean,
    resolving: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (resolving && !reducedMotion) 1.14f else 1f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = 520f)
    )
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(width = 46.dp, height = 13.dp)
                .offset(y = 22.dp)
                .clip(CircleShape)
                .background(InkForest.copy(alpha = 0.18f))
        )
        Image(
            painter = painterResource(R.drawable.sprite_1),
            contentDescription = stringResource(R.string.cd_tiger),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(58.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}

@Composable
private fun FloatingControls(
    state: GameUiState,
    onBank: () -> Unit,
    onHowTo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfacePlank.copy(alpha = 0.95f),
        border = BorderStroke(1.5.dp, Sage),
        shadowElevation = 14.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 16.dp, vertical = 12.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.strip_step, state.step),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss
                )
                Text(
                    text = stringResource(R.string.strip_next, state.nextReward),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss
                )
                Text(
                    text = stringResource(R.string.strip_banked, formatTokens(state.bankedTotal)),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when {
                    state.message != null -> stringResource(state.message)
                    state.phase == GamePhase.BLOCKED -> stringResource(R.string.hint_blocked)
                    state.canBank -> stringResource(R.string.hint_bank_or_leap)
                    else -> stringResource(R.string.hint_tap_stone)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = InkMoss,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            PrimaryButton(
                text = stringResource(R.string.bank_tokens),
                onClick = onBank,
                leadingIcon = R.drawable.ic_token,
                enabled = state.canBank,
                height = 56.dp,
                gradient = listOf(TigerOrange, TigerOrangeDeep),
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(onClick = onHowTo) {
                Text(
                    text = stringResource(R.string.how_to_play),
                    style = MaterialTheme.typography.labelLarge,
                    color = InkMoss
                )
            }
        }
    }
}
