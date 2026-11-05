package com.Dopxafi4.Wewwem.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.theme.HazardClay
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.JungleLight
import com.Dopxafi4.Wewwem.core.theme.Overlays
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.Terracotta
import com.Dopxafi4.Wewwem.core.theme.TigerOrange
import com.Dopxafi4.Wewwem.core.ui.AppBackground
import com.Dopxafi4.Wewwem.core.ui.HubTile
import com.Dopxafi4.Wewwem.core.ui.PlankCard
import com.Dopxafi4.Wewwem.core.ui.PrimaryButton
import com.Dopxafi4.Wewwem.core.ui.RouteChip
import com.Dopxafi4.Wewwem.core.ui.TokenBadge
import com.Dopxafi4.Wewwem.presentation.common.RouteUi

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }

    MenuContent(
        state = state,
        onPlay = onPlay,
        onDaily = onDaily,
        onProgress = onProgress,
        onRewards = onRewards,
        onSettings = onSettings,
        onSelectRoute = menuViewModel::selectRoute
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit,
    onSelectRoute: (String) -> Unit
) {
    AppBackground(overlay = Overlays.menu) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.app_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = InkForest
                    )
                    Text(
                        text = stringResource(R.string.hub_subtitle),
                        style = MaterialTheme.typography.labelSmall,
                        color = InkMoss
                    )
                }
                TokenBadge(tokens = state.tokens)
            }

            HeroTile(
                route = state.selectedRoute,
                onPlay = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HubTile(
                    icon = R.drawable.ic_calendar,
                    title = stringResource(R.string.tile_daily),
                    caption = if (state.dailyDone) {
                        stringResource(R.string.caption_done)
                    } else {
                        stringResource(R.string.caption_ready)
                    },
                    accent = TigerOrange,
                    onClick = onDaily,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                HubTile(
                    icon = R.drawable.ic_chart,
                    title = stringResource(R.string.tile_progress),
                    caption = if (state.bestSteps > 0) {
                        stringResource(R.string.caption_best_steps, state.bestSteps)
                    } else {
                        stringResource(R.string.caption_no_runs)
                    },
                    accent = JungleGreen,
                    onClick = onProgress,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HubTile(
                    icon = R.drawable.ic_gift,
                    title = stringResource(R.string.tile_rewards),
                    caption = if (state.claimableRewards > 0) {
                        stringResource(R.string.caption_rewards_ready, state.claimableRewards)
                    } else {
                        stringResource(R.string.caption_locked)
                    },
                    accent = Terracotta,
                    onClick = onRewards,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                HubTile(
                    icon = R.drawable.ic_gear,
                    title = stringResource(R.string.tile_settings),
                    caption = stringResource(R.string.caption_options),
                    accent = Sage,
                    onClick = onSettings,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.routes.forEach { route ->
                    RouteChip(
                        title = stringResource(route.titleRes),
                        caption = if (route.locked) {
                            stringResource(R.string.chip_locked)
                        } else {
                            stringResource(R.string.chip_steps, route.rows)
                        },
                        selected = route.id == state.selectedRouteId,
                        locked = route.locked,
                        stateLabel = if (route.id == state.selectedRouteId) {
                            stringResource(R.string.state_selected)
                        } else {
                            stringResource(R.string.state_not_selected)
                        },
                        onClick = { onSelectRoute(route.id) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }

            if (state.message != null) {
                Text(
                    text = stringResource(state.message, state.messageArg),
                    style = MaterialTheme.typography.labelSmall,
                    color = HazardClay,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun HeroTile(
    route: RouteUi?,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val glow by transition.animateFloat(
        initialValue = 0.16f,
        targetValue = 0.34f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    PlankCard(
        modifier = modifier,
        borderColor = JungleGreen.copy(alpha = 0.40f),
        corner = 20.dp,
        elevation = 10.dp,
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(TigerOrange.copy(alpha = glow), Color.Transparent)
                        )
                    )
            )
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(96.dp)
            )
        }
        Text(
            text = if (route != null) {
                stringResource(route.titleRes)
            } else {
                stringResource(R.string.route_bamboo)
            },
            style = MaterialTheme.typography.headlineSmall,
            color = InkForest,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(
                R.string.route_summary,
                route?.tiles ?: 3,
                route?.rows ?: 8
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = InkMoss,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(R.string.play),
            onClick = onPlay,
            leadingIcon = R.drawable.ic_play,
            gradient = listOf(JungleGreen, JungleLight),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
