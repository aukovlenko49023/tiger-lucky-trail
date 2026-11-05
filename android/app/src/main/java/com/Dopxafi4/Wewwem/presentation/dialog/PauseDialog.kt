package com.Dopxafi4.Wewwem.presentation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.theme.HazardClay

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    ConfirmDialog(
        title = stringResource(R.string.pause_title),
        body = stringResource(R.string.pause_body),
        confirmText = stringResource(R.string.menu),
        dismissText = stringResource(R.string.resume),
        onConfirm = onMenu,
        onDismiss = onResume,
        confirmColor = HazardClay
    )
}
