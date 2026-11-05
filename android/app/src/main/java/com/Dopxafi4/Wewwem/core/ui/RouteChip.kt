package com.Dopxafi4.Wewwem.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.core.theme.InkForest
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.Sage
import com.Dopxafi4.Wewwem.core.theme.SurfacePlank
import com.Dopxafi4.Wewwem.core.theme.TigerOrange

@Composable
fun RouteChip(
    title: String,
    caption: String,
    selected: Boolean,
    locked: Boolean,
    stateLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val border = when {
        selected -> BorderStroke(2.dp, TigerOrange)
        else -> BorderStroke(1.dp, Sage)
    }
    val fill = when {
        selected -> TigerOrange.copy(alpha = 0.16f)
        else -> SurfacePlank.copy(alpha = 0.72f)
    }
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = fill,
        border = border,
        modifier = modifier
            .alpha(if (locked) 0.55f else 1f)
            .semantics { stateDescription = stateLabel }
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (locked) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterEnd)
                        .alpha(0.25f)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (locked) {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null,
                            tint = InkMoss,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = InkForest,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(3.dp))
                Text(
                    text = caption,
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMoss,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
