package com.Dopxafi4.Wewwem.presentation.daily

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.config.GameConfig
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.JungleLight
import com.Dopxafi4.Wewwem.core.theme.Overlays
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.TigerOrange
import com.Dopxafi4.Wewwem.core.theme.TigerOrangeDeep
import com.Dopxafi4.Wewwem.core.ui.AppBackground
import com.Dopxafi4.Wewwem.core.ui.GlyphIconButton
import com.Dopxafi4.Wewwem.core.ui.PlankCard
import com.Dopxafi4.Wewwem.core.ui.PrimaryButton
import com.Dopxafi4.Wewwem.core.ui.SecondaryButton
import com.Dopxafi4.Wewwem.core.ui.SectionHeader
import com.Dopxafi4.Wewwem.domain.model.DailyGoal

@Composable
fun DailyChallengeScreen(
    onStart: () -> Unit,
    onMenu: () -> Unit
) {
    val dailyViewModel: DailyChallengeViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by dailyViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dailyViewModel.refresh()
    }

    DailyChallengeContent(
        state = state,
        onStart = {
            dailyViewModel.startChallenge()
            onStart()
        },
        onClaim = dailyViewModel::claimReward,
        onMenu = onMenu
    )
}

@Composable
private fun DailyChallengeContent(
    state: DailyChallengeUiState,
    onStart: () -> Unit,
    onClaim: () -> Unit,
    onMenu: () -> Unit
) {
    AppBackground(overlay = Overlays.daily) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                    title = stringResource(R.string.daily_title),
                    caption = stringResource(R.string.daily_caption),
                    modifier = Modifier.weight(1f)
                )
            }

            PlankCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TigerOrange.copy(alpha = 0.5f)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_hazard),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(84.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = when (state.goal) {
                        DailyGoal.REACH_STEP -> stringResource(
                            R.string.daily_goal_step,
                            state.target,
                            stringResource(state.routeTitleRes)
                        )

                        DailyGoal.BANK_TOKENS -> stringResource(
                            R.string.daily_goal_tokens,
                            state.target
                        )

                        DailyGoal.COMPLETE_RUNS -> stringResource(
                            R.string.daily_goal_runs,
                            state.target
                        )
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    color = InkForest,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { state.fraction },
                    color = TigerOrange,
                    trackColor = Sage.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(
                        R.string.daily_progress,
                        state.progress,
                        state.target
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            PlankCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = stringResource(R.string.cd_reward_crate),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = stringResource(
                                R.string.daily_reward_tokens,
                                GameConfig.DAILY_TOKEN_REWARD
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = InkForest
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = stringResource(R.string.daily_reward_shield),
                            style = MaterialTheme.typography.labelSmall,
                            color = InkMoss
                        )
                    }
                }
            }

            if (state.claimed) {
                PlankCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = JungleGreen.copy(alpha = 0.5f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            tint = JungleGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.daily_come_back),
                            style = MaterialTheme.typography.labelLarge,
                            color = InkForest
                        )
                    }
                }
            } else if (state.completed) {
                PrimaryButton(
                    text = stringResource(R.string.claim_reward),
                    onClick = onClaim,
                    leadingIcon = R.drawable.ic_gift,
                    gradient = listOf(TigerOrange, TigerOrangeDeep),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PrimaryButton(
                    text = stringResource(R.string.start_challenge),
                    onClick = onStart,
                    leadingIcon = R.drawable.ic_play,
                    gradient = listOf(JungleGreen, JungleLight),
                    modifier = Modifier.fillMaxWidth()
                )
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
