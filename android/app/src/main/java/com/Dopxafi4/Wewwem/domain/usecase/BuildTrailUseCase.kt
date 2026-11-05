package com.Dopxafi4.Wewwem.domain.usecase

import com.Dopxafi4.Wewwem.domain.model.Route
import com.Dopxafi4.Wewwem.domain.model.StoneState
import com.Dopxafi4.Wewwem.domain.model.TrailRow
import kotlin.random.Random

class BuildTrailUseCase {

    operator fun invoke(route: Route, random: Random = Random.Default): List<TrailRow> =
        List(route.rows) {
            val hazards = mutableSetOf<Int>()
            val wanted = route.hazards.coerceAtMost(route.tiles - 1)
            while (hazards.size < wanted) {
                hazards.add(random.nextInt(route.tiles))
            }
            TrailRow(
                hazards = hazards.toSet(),
                stones = List(route.tiles) { StoneState.UNTOUCHED }
            )
        }
}
