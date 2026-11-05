package com.Dopxafi4.Wewwem.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TigerColorScheme = lightColorScheme(
    primary = JungleGreen,
    onPrimary = Cream,
    primaryContainer = JungleLight,
    onPrimaryContainer = Cream,
    secondary = TigerOrange,
    onSecondary = InkForest,
    secondaryContainer = TigerOrangeDeep,
    onSecondaryContainer = InkForest,
    tertiary = Terracotta,
    onTertiary = Cream,
    background = LeafCream,
    onBackground = InkForest,
    surface = SurfacePlank,
    onSurface = InkForest,
    surfaceVariant = SurfaceSand,
    onSurfaceVariant = InkMoss,
    outline = Sage,
    outlineVariant = Sage,
    error = HazardClay,
    onError = Cream
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TigerColorScheme,
        typography = TigerTypography,
        content = content
    )
}
