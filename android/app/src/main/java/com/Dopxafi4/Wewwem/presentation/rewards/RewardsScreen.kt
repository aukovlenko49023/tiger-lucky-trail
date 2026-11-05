package com.Dopxafi4.Wewwem.presentation.rewards

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.theme.Cream
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.Overlays
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.SurfacePlank
import com.Dopxafi4.Wewwem.core.theme.Terracotta
import com.Dopxafi4.Wewwem.core.theme.TigerOrange
import com.Dopxafi4.Wewwem.core.theme.TigerOrangeDeep
import com.Dopxafi4.Wewwem.core.ui.AppBackground
import com.Dopxafi4.Wewwem.core.ui.GlyphIconButton
import com.Dopxafi4.Wewwem.core.ui.PlankCard
import com.Dopxafi4.Wewwem.core.ui.PrimaryButton
import com.Dopxafi4.Wewwem.core.ui.SecondaryButton
import com.Dopxafi4.Wewwem.core.ui.SectionHeader
import com.Dopxafi4.Wewwem.core.ui.TokenBadge

@Composable
fun RewardsScreen(onMenu: () -> Unit) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by rewardsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rewardsViewModel.refresh()
    }

    RewardsContent(
        state = state,
        onSelectTab = rewardsViewModel::selectTab,
        onClaim = rewardsViewModel::claimTier,
        onMenu = onMenu
    )
}

@Composable
private fun RewardsContent(
    state: RewardsUiState,
    onSelectTab: (RewardsTab) -> Unit,
    onClaim: (String) -> Unit,
    onMenu: () -> Unit
) {
    AppBackground(overlay = Overlays.rewards) {
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
                    title = stringResource(R.string.rewards_title),
                    modifier = Modifier.weight(1f)
                )
                TokenBadge(tokens = state.tokens)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SegmentButton(
                    label = stringResource(R.string.tab_rewards),
                    selected = state.tab == RewardsTab.REWARDS,
                    onClick = { onSelectTab(RewardsTab.REWARDS) },
                    modifier = Modifier.weight(1f)
                )
                SegmentButton(
                    label = stringResource(R.string.tab_collection),
                    selected = state.tab == RewardsTab.COLLECTION,
                    onClick = { onSelectTab(RewardsTab.COLLECTION) },
                    modifier = Modifier.weight(1f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Crossfade(targetState = state.tab) { tab ->
                    when (tab) {
                        RewardsTab.REWARDS -> Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            state.tiers.forEach { tier ->
                                RewardRow(tier = tier, onClaim = { onClaim(tier.id) })
                            }
                        }

                        RewardsTab.COLLECTION -> Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            state.charms.chunked(3).forEach { chunk ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    chunk.forEach { charm ->
                                        CharmTile(
                                            charm = charm,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
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
private fun SegmentButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (selected) JungleGreen else SurfacePlank.copy(alpha = 0.85f),
        border = BorderStroke(1.5.dp, if (selected) JungleGreen else Sage),
        modifier = modifier
            .fillMaxSize()
            .semantics {
                stateDescription = label
            }
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) Cream else InkMoss
            )
        }
    }
}

@Composable
private fun RewardRow(
    tier: RewardTierUi,
    onClaim: () -> Unit
) {
    PlankCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (tier.claimed) JungleGreen.copy(alpha = 0.5f) else Sage,
        corner = 16.dp,
        elevation = 6.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_reward_crate),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(46.dp)
                    .alpha(if (tier.reached) 1f else 0.5f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(tier.titleRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = InkForest
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(R.string.tier_requirement, tier.requirement),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            when {
                tier.claimed -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = JungleGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.tier_claimed),
                        style = MaterialTheme.typography.labelSmall,
                        color = JungleGreen
                    )
                }

                tier.reached -> PrimaryButton(
                    text = stringResource(R.string.claim),
                    onClick = onClaim,
                    height = 44.dp,
                    gradient = listOf(TigerOrange, TigerOrangeDeep),
                    modifier = Modifier.width(104.dp)
                )

                else -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = InkMoss,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.tier_to_go, tier.remaining),
                        style = MaterialTheme.typography.labelSmall,
                        color = InkMoss
                    )
                }
            }
        }
    }
}

@Composable
private fun CharmTile(
    charm: CharmUi,
    modifier: Modifier = Modifier
) {
    PlankCard(
        modifier = modifier,
        borderColor = if (charm.unlocked) Terracotta.copy(alpha = 0.55f) else Sage,
        corner = 16.dp,
        elevation = 5.dp
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(52.dp)
                .align(Alignment.CenterHorizontally)
                .alpha(if (charm.unlocked) 1f else 0.28f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            if (!charm.unlocked) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = InkMoss,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = if (charm.unlocked) {
                    stringResource(charm.titleRes)
                } else {
                    stringResource(R.string.charm_locked)
                },
                style = MaterialTheme.typography.labelSmall,
                color = InkForest,
                textAlign = TextAlign.Center
            )
        }
    }
}
