package com.Dopxafi4.Wewwem.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Dopxafi4.Wewwem.core.theme.InkMoss
import com.Dopxafi4.Wewwem.core.theme.SurfaceSand

@Composable
fun StatCard(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    @DrawableRes image: Int? = null
) {
    PlankCard(
        modifier = modifier,
        borderColor = accent.copy(alpha = 0.55f),
        containerColor = SurfaceSand.copy(alpha = 0.62f),
        corner = 16.dp,
        elevation = 5.dp,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp)
    ) {
        if (image != null) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(accent, CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 22.sp,
            color = accent,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = InkMoss,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
