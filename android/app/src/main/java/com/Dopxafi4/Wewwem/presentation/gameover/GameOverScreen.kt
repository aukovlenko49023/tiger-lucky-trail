package com.Dopxafi4.Wewwem.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.theme.HazardClay
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleDeep
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.JungleLight
import com.Dopxafi4.Wewwem.core.theme.Overlays
import com.Dopxafi4.Wewwem.core.theme.Terracotta
import com.Dopxafi4.Wewwem.core.theme.TigerOrange
import com.Dopxafi4.Wewwem.core.ui.AppBackground
import com.Dopxafi4.Wewwem.core.ui.GlyphIconButton
import com.Dopxafi4.Wewwem.core.ui.PlankCard
import com.Dopxafi4.Wewwem.core.ui.PrimaryButton
import com.Dopxafi4.Wewwem.core.ui.SecondaryButton
import com.Dopxafi4.Wewwem.core.ui.StatCard
import com.Dopxafi4.Wewwem.domain.formatTokens

@Composable
fun GameOverScreen(
    runId: Long,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(runId) {
        gameOverViewModel.load()
    }

    GameOverContent(
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(state.tokens) {
        revealed = true
    }
    val countedTokens by animateIntAsState(
        targetValue = if (revealed) state.tokens else 0,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )
    val transition = rememberInfiniteTransition()
    val breath by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    AppBackground(overlay = if (state.banked) Overlays.banked else Overlays.blocked) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                GlyphIconButton(
                    image = R.drawable.icon_home,
                    contentDescription = stringResource(R.string.cd_menu),
                    onClick = onMenu
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(state.routeTitleRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = InkForest
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(
                        if (state.banked) R.drawable.sprite_4 else R.drawable.sprite_3
                    ),
                    contentDescription = stringResource(
                        if (state.banked) R.string.cd_reward_crate else R.string.cd_hazard
                    ),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(96.dp)
                        .graphicsLayer {
                            scaleX = breath
                            scaleY = breath
                        }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(
                        if (state.banked) R.string.result_banked else R.string.result_blocked
                    ),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 32.sp,
                    color = if (state.banked) JungleDeep else HazardClay,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(
                        R.string.result_subtitle,
                        state.steps,
                        state.totalSteps
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMoss,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (state.visibleStats >= 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (state.tokens > 0) {
                            StatCard(
                                value = formatTokens(countedTokens),
                                label = stringResource(
                                    if (state.banked) {
                                        R.string.stat_tokens_banked
                                    } else {
                                        R.string.stat_tokens_lost
                                    }
                                ),
                                accent = TigerOrange,
                                image = R.drawable.sprite_2,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (state.steps > 0) {
                            StatCard(
                                value = state.steps.toString(),
                                label = stringResource(R.string.stat_steps),
                                accent = JungleGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (state.bestSteps > 0) {
                            StatCard(
                                value = state.bestSteps.toString(),
                                label = stringResource(R.string.stat_best),
                                accent = Terracotta,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    PlankCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.result_empty_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkMoss,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (state.newRecord) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = TigerOrange.copy(alpha = 0.20f),
                        border = BorderStroke(1.5.dp, TigerOrange)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_trophy),
                                contentDescription = null,
                                tint = TigerOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.new_record),
                                style = MaterialTheme.typography.labelLarge,
                                color = InkForest
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    PrimaryButton(
                        text = stringResource(R.string.play_again),
                        onClick = onPlayAgain,
                        leadingIcon = R.drawable.ic_play,
                        gradient = listOf(JungleGreen, JungleLight),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SecondaryButton(
                        text = stringResource(R.string.menu),
                        onClick = onMenu,
                        leadingImage = R.drawable.icon_home,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
