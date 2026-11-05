package com.Dopxafi4.Wewwem.core.theme

import androidx.compose.ui.graphics.Brush

object Overlays {

    val splash: Brush = Brush.verticalGradient(
        listOf(
            InkForest.copy(alpha = 0.93f),
            JungleDeep.copy(alpha = 0.89f),
            InkForest.copy(alpha = 0.95f)
        )
    )

    val menu: Brush = Brush.verticalGradient(
        listOf(
            LeafCream.copy(alpha = 0.87f),
            LeafCreamDeep.copy(alpha = 0.81f),
            LeafCream.copy(alpha = 0.93f)
        )
    )

    val game: Brush = Brush.verticalGradient(
        listOf(
            JungleDeep.copy(alpha = 0.58f),
            LeafCream.copy(alpha = 0.83f),
            LeafCreamDeep.copy(alpha = 0.89f)
        )
    )

    val banked: Brush = Brush.verticalGradient(
        listOf(
            JungleGreen.copy(alpha = 0.58f),
            LeafCream.copy(alpha = 0.90f),
            LeafCreamDeep.copy(alpha = 0.93f)
        )
    )

    val blocked: Brush = Brush.verticalGradient(
        listOf(
            HazardClay.copy(alpha = 0.45f),
            LeafCream.copy(alpha = 0.90f),
            LeafCreamDeep.copy(alpha = 0.93f)
        )
    )

    val daily: Brush = Brush.verticalGradient(
        listOf(
            LeafCream.copy(alpha = 0.89f),
            TigerOrange.copy(alpha = 0.18f),
            LeafCreamDeep.copy(alpha = 0.93f)
        )
    )

    val progress: Brush = Brush.verticalGradient(
        listOf(
            LeafCreamDeep.copy(alpha = 0.91f),
            LeafCream.copy(alpha = 0.93f)
        )
    )

    val rewards: Brush = Brush.verticalGradient(
        listOf(
            LeafCream.copy(alpha = 0.91f),
            Sage.copy(alpha = 0.22f),
            LeafCreamDeep.copy(alpha = 0.93f)
        )
    )

    val settings: Brush = Brush.verticalGradient(
        listOf(
            LeafCreamDeep.copy(alpha = 0.93f),
            LeafCream.copy(alpha = 0.95f)
        )
    )
}
