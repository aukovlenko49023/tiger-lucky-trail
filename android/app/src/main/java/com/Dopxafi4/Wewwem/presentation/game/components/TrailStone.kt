package com.Dopxafi4.Wewwem.presentation.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.theme.HazardClay
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleDeep
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.SurfacePlank
import com.Dopxafi4.Wewwem.core.theme.TigerOrange
import com.Dopxafi4.Wewwem.domain.model.StoneState

@Composable
fun TrailStone(
    state: StoneState,
    reward: Int,
    active: Boolean,
    showHint: Boolean,
    enabled: Boolean,
    glow: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fill = when (state) {
        StoneState.SAFE -> JungleGreen.copy(alpha = 0.24f)
        StoneState.HAZARD -> HazardClay.copy(alpha = 0.22f)
        StoneState.SHIELDED -> JungleGreen.copy(alpha = 0.18f)
        StoneState.PASSED -> SurfacePlank.copy(alpha = 0.72f)
        StoneState.UNTOUCHED -> SurfacePlank.copy(alpha = 0.90f)
    }
    val border = when {
        state == StoneState.HAZARD -> BorderStroke(2.dp, HazardClay)
        state == StoneState.SAFE -> BorderStroke(2.dp, JungleGreen)
        state == StoneState.SHIELDED -> BorderStroke(2.5.dp, JungleGreen)
        active -> BorderStroke(2.5.dp, TigerOrange.copy(alpha = glow))
        else -> BorderStroke(2.dp, Sage)
    }
    val label = when (state) {
        StoneState.SAFE -> stringResource(R.string.state_stone_safe, reward)
        StoneState.HAZARD -> stringResource(R.string.state_stone_hazard)
        StoneState.SHIELDED -> stringResource(R.string.state_stone_shielded)
        StoneState.PASSED -> stringResource(R.string.state_stone_passed)
        StoneState.UNTOUCHED -> stringResource(R.string.state_stone_untouched)
    }
    val canTap = enabled && active && state == StoneState.UNTOUCHED

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = fill,
        border = border,
        shadowElevation = if (state == StoneState.UNTOUCHED) 4.dp else 1.dp,
        modifier = modifier
            .alpha(if (state == StoneState.PASSED) 0.55f else 1f)
            .semantics { stateDescription = label }
            .clickable(enabled = canTap, role = Role.Button, onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                StoneState.SAFE -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = stringResource(R.string.reward_plus, reward),
                        style = MaterialTheme.typography.labelLarge,
                        color = JungleDeep
                    )
                }

                StoneState.HAZARD -> Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_hazard),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(38.dp)
                )

                StoneState.SHIELDED -> Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_hazard),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(34.dp)
                        .alpha(0.55f)
                )

                else -> Icon(
                    painter = painterResource(R.drawable.ic_leaf),
                    contentDescription = null,
                    tint = if (showHint) JungleGreen else InkMoss,
                    modifier = Modifier
                        .size(if (showHint) 22.dp else 18.dp)
                        .alpha(if (showHint) 0.65f else 0.22f)
                )
            }
        }
    }
}
