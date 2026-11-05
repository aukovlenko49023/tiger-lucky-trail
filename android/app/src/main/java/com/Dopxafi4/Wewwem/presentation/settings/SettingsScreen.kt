package com.Dopxafi4.Wewwem.presentation.settings

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.di.AppViewModelFactory
import com.Dopxafi4.Wewwem.core.theme.Cream
import com.Dopxafi4.Wewwem.core.theme.HazardClay
import com.Dopxafi4.Wewwem.core.theme.InkFaded
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.Overlays
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.SurfacePlank
import com.Dopxafi4.Wewwem.core.ui.AppBackground
import com.Dopxafi4.Wewwem.core.ui.GlyphIconButton
import com.Dopxafi4.Wewwem.core.ui.PlankCard
import com.Dopxafi4.Wewwem.core.ui.SecondaryButton
import com.Dopxafi4.Wewwem.core.ui.SectionHeader
import com.Dopxafi4.Wewwem.presentation.dialog.ConfirmDialog

@Composable
fun SettingsScreen(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    var showConfirm by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        settingsViewModel.refresh()
    }

    SettingsContent(
        state = state,
        onHaptics = settingsViewModel::setHaptics,
        onReducedMotion = settingsViewModel::setReducedMotion,
        onStoneHints = settingsViewModel::setStoneHints,
        onReset = { showConfirm = true },
        onMenu = onMenu
    )

    if (showConfirm) {
        ConfirmDialog(
            title = stringResource(R.string.reset_title),
            body = stringResource(R.string.reset_body),
            confirmText = stringResource(R.string.reset),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                settingsViewModel.resetProgress()
                showConfirm = false
            },
            onDismiss = { showConfirm = false },
            confirmColor = HazardClay
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onHaptics: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onStoneHints: (Boolean) -> Unit,
    onReset: () -> Unit,
    onMenu: () -> Unit
) {
    AppBackground(overlay = Overlays.settings) {
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
                    title = stringResource(R.string.settings_title),
                    caption = stringResource(R.string.settings_caption),
                    modifier = Modifier.weight(1f)
                )
            }

            SettingRow(
                title = stringResource(R.string.setting_haptics),
                body = stringResource(R.string.setting_haptics_body),
                checked = state.haptics,
                onCheckedChange = onHaptics
            )

            SettingRow(
                title = stringResource(R.string.setting_motion),
                body = stringResource(R.string.setting_motion_body),
                checked = state.reducedMotion,
                onCheckedChange = onReducedMotion
            )

            SettingRow(
                title = stringResource(R.string.setting_hints),
                body = stringResource(R.string.setting_hints_body),
                checked = state.stoneHints,
                onCheckedChange = onStoneHints
            )

            if (state.resetDone) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = JungleGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.reset_done),
                        style = MaterialTheme.typography.labelSmall,
                        color = JungleGreen
                    )
                }
            }

            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, HazardClay),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SurfacePlank.copy(alpha = 0.8f),
                    contentColor = HazardClay
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_progress),
                    style = MaterialTheme.typography.labelLarge,
                    color = HazardClay
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = stringResource(R.string.settings_footer),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkFaded,
                    textAlign = TextAlign.Center,
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

@Composable
private fun SettingRow(
    title: String,
    body: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val stateLabel = if (checked) {
        stringResource(R.string.state_on)
    } else {
        stringResource(R.string.state_off)
    }
    PlankCard(
        modifier = Modifier.fillMaxWidth(),
        corner = 16.dp,
        elevation = 6.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = InkForest
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = body,
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Cream,
                    checkedTrackColor = JungleGreen,
                    uncheckedThumbColor = SurfacePlank,
                    uncheckedTrackColor = Sage
                ),
                modifier = Modifier.semantics { stateDescription = stateLabel }
            )
        }
    }
}
