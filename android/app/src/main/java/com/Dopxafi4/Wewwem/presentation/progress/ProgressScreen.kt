package com.Dopxafi4.Wewwem.presentation.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.Overlays
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.Terracotta
import com.Dopxafi4.Wewwem.core.theme.TigerOrange
import com.Dopxafi4.Wewwem.core.ui.AppBackground
import com.Dopxafi4.Wewwem.core.ui.GlyphIconButton
import com.Dopxafi4.Wewwem.core.ui.PlankCard
import com.Dopxafi4.Wewwem.core.ui.SecondaryButton
import com.Dopxafi4.Wewwem.core.ui.SectionHeader
import com.Dopxafi4.Wewwem.core.ui.StatCard
import com.Dopxafi4.Wewwem.domain.formatTokens
import com.Dopxafi4.Wewwem.presentation.common.RouteUi

@Composable
fun ProgressScreen(onMenu: () -> Unit) {
    val progressViewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by progressViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        progressViewModel.refresh()
    }

    ProgressContent(state = state, onMenu = onMenu)
}

@Composable
private fun ProgressContent(
    state: ProgressUiState,
    onMenu: () -> Unit
) {
    AppBackground(overlay = Overlays.progress) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlyphIconButton(
                    image = R.drawable.icon_home,
                    contentDescription = stringResource(R.string.cd_menu),
                    onClick = onMenu
                )
                Spacer(modifier = Modifier.width(12.dp))
                SectionHeader(
                    title = stringResource(R.string.progress_title),
                    caption = stringResource(R.string.progress_caption),
                    modifier = Modifier.weight(1f)
                )
            }

            if (state.visibleStats >= 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (state.lifetimeTokens > 0) {
                        StatCard(
                            value = formatTokens(state.lifetimeTokens),
                            label = stringResource(R.string.stat_lifetime),
                            accent = TigerOrange,
                            image = R.drawable.sprite_2,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.totalRuns > 0) {
                        StatCard(
                            value = state.totalRuns.toString(),
                            label = stringResource(R.string.stat_runs),
                            accent = JungleGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.bestSteps > 0) {
                        StatCard(
                            value = state.bestSteps.toString(),
                            label = stringResource(R.string.stat_best_steps),
                            accent = Terracotta,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                PlankCard(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_paw),
                        contentDescription = null,
                        tint = JungleGreen,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.progress_empty_title),
                        style = MaterialTheme.typography.labelLarge,
                        color = InkForest,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.progress_empty_body),
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMoss,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.routes.forEach { route ->
                    RouteRow(route = route, lifetimeTokens = state.lifetimeTokens)
                }
            }

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                leadingImage = R.drawable.icon_home,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RouteRow(
    route: RouteUi,
    lifetimeTokens: Int
) {
    PlankCard(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (route.locked) 0.6f else 1f),
        borderColor = if (route.locked) Sage else JungleGreen.copy(alpha = 0.45f),
        corner = 16.dp,
        elevation = 6.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (route.locked) {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null,
                            tint = InkMoss,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = stringResource(route.titleRes),
                        style = MaterialTheme.typography.labelLarge,
                        color = InkForest
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(
                        R.string.route_detail,
                        route.tiles,
                        route.rows,
                        route.baseReward
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (route.locked) {
                    LinearProgressIndicator(
                        progress = {
                            if (route.unlockTokens <= 0) {
                                1f
                            } else {
                                (lifetimeTokens.toFloat() / route.unlockTokens.toFloat())
                                    .coerceIn(0f, 1f)
                            }
                        },
                        color = TigerOrange,
                        trackColor = Sage.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.route_unlock_at, route.unlockTokens),
                        style = MaterialTheme.typography.labelSmall,
                        color = InkMoss
                    )
                } else {
                    Text(
                        text = if (route.bestSteps > 0) {
                            stringResource(R.string.route_best, route.bestSteps)
                        } else {
                            stringResource(R.string.route_no_best)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = JungleGreen
                    )
                }
            }
        }
    }
}
