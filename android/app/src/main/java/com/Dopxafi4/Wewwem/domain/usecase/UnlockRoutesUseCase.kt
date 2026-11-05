package com.Dopxafi4.Wewwem.domain.usecase

import com.Dopxafi4.Wewwem.domain.model.Route

class UnlockRoutesUseCase {

    operator fun invoke(route: Route, lifetimeTokens: Int): Boolean =
        lifetimeTokens >= route.unlockTokens

    fun remaining(route: Route, lifetimeTokens: Int): Int =
        (route.unlockTokens - lifetimeTokens).coerceAtLeast(0)
}
