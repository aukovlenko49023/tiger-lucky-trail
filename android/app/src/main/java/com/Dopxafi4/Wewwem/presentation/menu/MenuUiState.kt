package com.Dopxafi4.Wewwem.presentation.menu

import androidx.annotation.StringRes
import com.Dopxafi4.Wewwem.data.sample.SampleData
import com.Dopxafi4.Wewwem.presentation.common.RouteUi

data class MenuUiState(
    val tokens: Int = 0,
    val routes: List<RouteUi> = emptyList(),
    val selectedRouteId: String = SampleData.ROUTE_BAMBOO,
    val bestSteps: Int = 0,
    val dailyDone: Boolean = false,
    val claimableRewards: Int = 0,
    @StringRes val message: Int? = null,
    val messageArg: Int = 0
) {
    val selectedRoute: RouteUi? get() = routes.firstOrNull { it.id == selectedRouteId }
}
