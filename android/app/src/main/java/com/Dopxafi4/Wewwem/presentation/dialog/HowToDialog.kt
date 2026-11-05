package com.Dopxafi4.Wewwem.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.JungleGreen
import com.Dopxafi4.Wewwem.core.theme.SurfacePlank

@Composable
fun HowToDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = SurfacePlank,
        title = {
            Text(
                text = stringResource(R.string.how_to_play),
                style = MaterialTheme.typography.headlineSmall,
                color = InkForest
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.how_to_line_one),
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMoss
                )
                Text(
                    text = stringResource(R.string.how_to_line_two),
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMoss
                )
                Text(
                    text = stringResource(R.string.how_to_line_three),
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMoss
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.got_it),
                    style = MaterialTheme.typography.labelLarge,
                    color = JungleGreen
                )
            }
        }
    )
}
