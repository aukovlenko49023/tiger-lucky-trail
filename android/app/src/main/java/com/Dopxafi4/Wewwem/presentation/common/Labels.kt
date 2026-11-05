package com.Dopxafi4.Wewwem.presentation.common

import androidx.annotation.StringRes
import com.Dopxafi4.Wewwem.R
import com.Dopxafi4.Wewwem.data.sample.SampleData

object Labels {

    @StringRes
    fun route(routeId: String): Int = when (routeId) {
        SampleData.ROUTE_MIST -> R.string.route_mist
        SampleData.ROUTE_TEMPLE -> R.string.route_temple
        else -> R.string.route_bamboo
    }

    @StringRes
    fun tier(tierId: String): Int = when (tierId) {
        "lantern_cache" -> R.string.tier_lantern_cache
        "striped_chest" -> R.string.tier_striped_chest
        "fortune_hoard" -> R.string.tier_fortune_hoard
        else -> R.string.tier_jungle_satchel
    }

    @StringRes
    fun charm(charmId: String): Int = when (charmId) {
        "sun_lantern" -> R.string.charm_sun_lantern
        "bamboo_charm" -> R.string.charm_bamboo
        "mist_feather" -> R.string.charm_mist_feather
        "temple_coin" -> R.string.charm_temple_coin
        "golden_stripe" -> R.string.charm_golden_stripe
        else -> R.string.charm_jade_paw
    }
}
