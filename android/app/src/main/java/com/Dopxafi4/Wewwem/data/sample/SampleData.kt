package com.Dopxafi4.Wewwem.data.sample

import com.Dopxafi4.Wewwem.domain.model.Charm
import com.Dopxafi4.Wewwem.domain.model.RewardTier
import com.Dopxafi4.Wewwem.domain.model.Route

object SampleData {

    const val ROUTE_BAMBOO = "bamboo_grove"
    const val ROUTE_MIST = "mist_canopy"
    const val ROUTE_TEMPLE = "tiger_temple"

    val routes: List<Route> = listOf(
        Route(
            id = ROUTE_BAMBOO,
            tiles = 3,
            hazards = 1,
            rows = 8,
            baseReward = 10,
            growth = 5,
            summitBonus = 120,
            unlockTokens = 0
        ),
        Route(
            id = ROUTE_MIST,
            tiles = 3,
            hazards = 1,
            rows = 10,
            baseReward = 15,
            growth = 8,
            summitBonus = 220,
            unlockTokens = 600
        ),
        Route(
            id = ROUTE_TEMPLE,
            tiles = 4,
            hazards = 2,
            rows = 12,
            baseReward = 20,
            growth = 12,
            summitBonus = 400,
            unlockTokens = 1800
        )
    )

    val rewardTiers: List<RewardTier> = listOf(
        RewardTier(id = "jungle_satchel", requirement = 300, charmId = "jade_paw"),
        RewardTier(id = "lantern_cache", requirement = 900, charmId = "sun_lantern"),
        RewardTier(id = "striped_chest", requirement = 2000, charmId = "temple_coin"),
        RewardTier(id = "fortune_hoard", requirement = 4000, charmId = "golden_stripe")
    )

    val charms: List<Charm> = listOf(
        Charm(id = "jade_paw", tierId = "jungle_satchel"),
        Charm(id = "sun_lantern", tierId = "lantern_cache"),
        Charm(id = "bamboo_charm", tierId = "jungle_satchel"),
        Charm(id = "mist_feather", tierId = "lantern_cache"),
        Charm(id = "temple_coin", tierId = "striped_chest"),
        Charm(id = "golden_stripe", tierId = "fortune_hoard")
    )

    val routeIds: List<String> = routes.map { it.id }

    fun routeById(id: String): Route = routes.firstOrNull { it.id == id } ?: routes.first()
}
